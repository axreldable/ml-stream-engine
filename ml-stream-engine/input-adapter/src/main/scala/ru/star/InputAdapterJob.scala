package ru.star

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

object InputAdapterJob extends App {
  println("input-adapter started.")

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val params: Parameters = Parameters(args)

  val eventConsumer = new FlinkKafkaConsumer[String](
    "input-event-in", new SimpleStringSchema(), params.kafkaConsumerProperties
  )
  val eventProducer = new FlinkKafkaProducer[InternalEvent](
    "input-event-out", new InternalEventSerializer(), params.kafkaProducerProperties
  )

  val configConsumer = new FlinkKafkaConsumer[String](
    "input-config-in", new SimpleStringSchema(), params.kafkaConsumerProperties
  )
  val configProducer = new FlinkKafkaProducer[InternalConfig](
    "input-config-out", new InternalConfigSerializer(), params.kafkaProducerProperties
  )

  val modelConsumer = new FlinkKafkaConsumer[String](
    "input-model-in", new SimpleStringSchema(), params.kafkaConsumerProperties
  )
  val modelProducer = new FlinkKafkaProducer[InternalModel](
    "input-model-out", new InternalModelSerializer(), params.kafkaProducerProperties
  )

  InputAdapterBuilder(
    env = env,
    eventSource = eventConsumer,
    configSource = configConsumer,
    modelSource = modelConsumer,
    eventSink = eventProducer,
    configSink = configProducer,
    modelSink = modelProducer
  ).build()

  env.execute()
}
