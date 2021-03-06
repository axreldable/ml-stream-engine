package ru.star

import io.radicalbit.flink.pmml.scala.models.control.ServingMessage
import org.apache.kafka.common.serialization.StringSerializer

object GeneratorApp extends App {

  val params = GeneratorParams(args)

  val stringProducer = Helpers.initProducer[String](params.bootstrapServices, new StringSerializer().getClass.getName)
  val modelProducer = Helpers.initProducer[ServingMessage](params.bootstrapServices, new ServingMessageSerializer().getClass.getName)

  Helpers.sendModel("ml-stream-pmml-model-in", modelProducer)

  Helpers.sendMessages(
    tweetSource = "/tmp/data/training.1600000.processed.noemoticon.csv",
    tweetTopic = "ml-stream-input-adapter-message-in",
    irisTopic = "ml-stream-input-adapter-message-in",
    imageTopic = "ml-stream-input-adapter-message-in",
    producer = stringProducer
  )
}
