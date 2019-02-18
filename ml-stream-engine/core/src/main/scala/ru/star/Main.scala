package ru.star

import java.time.LocalDateTime
import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object Main extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  println("Start.")
  val params: Parameters = Parameters(args)
  params.kafkaConsumerProperties

  val properties: Properties = new Properties()
  properties.setProperty("bootstrap.servers", "localhost:9092")

  val stream: DataStream[InternalEvent] = env
    .addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties))
    .map(mes => {
      val event = InternalEvent(LocalDateTime.now().toString, mes)
      println(event)
      event
    })

  env.execute()
}
