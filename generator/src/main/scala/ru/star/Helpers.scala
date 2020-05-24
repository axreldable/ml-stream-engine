package ru.star

import java.io.{ByteArrayOutputStream, ObjectOutputStream}
import java.util
import java.util.Properties

import io.radicalbit.flink.pmml.scala.models.control.{AddMessage, ServingMessage}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}

import scala.io.Source
import scala.util.Random

object Helpers {

  def initProducer[T](bootstrapService: String, serializerName: String): KafkaProducer[String, T] = {
    val props = new Properties()

    props.put("bootstrap.servers", bootstrapService)
    props.put("key.serializer", new StringSerializer().getClass.getName)
    props.put("value.serializer", serializerName)
    val producer: KafkaProducer[String, T] = new KafkaProducer[String, T](props)
    producer.flush()

    println("Kafka producer initialized")

    producer
  }

  def sendModel(topic: String, producer: KafkaProducer[String, ServingMessage]): Unit = {
    val path = "/Users/axreldable/Desktop/projects/msu/msu-diploma-thesis/pmml-job/src/main/resources/kmeans.xml"
    val message = AddMessage(
      name = "123e4567-e89b-12d3-a456-426655440000",
      version = 1,
      path = path,
      occurredOn = System.currentTimeMillis()
    )

    producer.send(new ProducerRecord[String, ServingMessage](topic, message))
  }

  def sendMessages(tweetSource: String, tweetTopic: String, irisTopic: String, imageTopic: String,
                   producer: KafkaProducer[String, String]): Unit = {
    val separator = "###"
    val bufferedSource = Source.fromFile(tweetSource)
    for (line <- bufferedSource.getLines) {
      val tweet = line.split(",").last
      val message = s"tweet-type$separator$tweet"
      println(message)
      producer.send(new ProducerRecord[String, String](tweetTopic, message))

      val iris = s"iris-type$separator${createIris()}"
      println(iris)
      producer.send(new ProducerRecord[String, String](irisTopic, iris))

      val image = s"image-type$separator${createImage()}"
      println(image)
      producer.send(new ProducerRecord[String, String](imageTopic, image))

      Thread.sleep(1000)
    }

    bufferedSource.close
  }

  def createIris(): String = {
    val numberOfParams = 4
    val min = 0.2
    val max = 6.0

    def truncateDouble(n: Double) = (math floor n * 10) / 10

    def randomVal = min + (max - min) * Random.nextDouble()

    val dataForIris = Seq.fill(numberOfParams)(truncateDouble(randomVal))

    val iris = s"${dataForIris(0)},${dataForIris(1)},${dataForIris(2)},${dataForIris(3)}"
    iris
  }

  def createImage(): String = {
    if (Random.nextBoolean()) {
      "/Users/axreldable/Desktop/projects/msu/msu-diploma-thesis/service-ml-job/src/main/resources/pug.jpeg"
    } else {
      "/Users/axreldable/Desktop/projects/msu/msu-diploma-thesis/service-ml-job/src/main/resources/retriever.jpg"
    }
  }
}


class ServingMessageSerializer extends Serializer[ServingMessage] {
  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def serialize(topic: String, data: ServingMessage): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(data)
    oos.close()
    stream.toByteArray
  }

  override def close(): Unit = {}
}