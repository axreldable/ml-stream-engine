package ru.star

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

object MessageWorker extends App {
  println("message-worker started.")

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val params: Parameters = Parameters(args)

  val eventProducer = new FlinkKafkaProducer[String](
    "input-event-in", new SimpleStringSchema(), params.kafkaProducerProperties
  )

  val configProducer = new FlinkKafkaProducer[String](
    "input-config-in", new SimpleStringSchema(), params.kafkaProducerProperties
  )

  val modelProducer = new FlinkKafkaProducer[String](
    "input-model-in", new SimpleStringSchema(), params.kafkaProducerProperties
  )

  val alarmEventConsumer = new FlinkKafkaConsumer[String](
    "output-alarm-topic", new SimpleStringSchema(), params.kafkaConsumerProperties
  )

  val otherEventConsumer = new FlinkKafkaConsumer[String](
    "output-other-topic", new SimpleStringSchema(), params.kafkaConsumerProperties
  )

  MessageWorkerBuilder(
    env = env,
    eventSink = eventProducer,
    configSink = configProducer,
    modelSink = modelProducer,
    alarmSource = alarmEventConsumer,
    otherSource = otherEventConsumer
  ).build()

  env.execute()
}
