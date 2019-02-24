package ru.star

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

object InputAdapterJob extends App {
  println("input-adapter started.")

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val params: Parameters = Parameters(args)

  val stringEventConsumer = new FlinkKafkaConsumer[String](
    "input-adapter-in", new SimpleStringSchema(), params.kafkaConsumerProperties
  )

  val eventProducer = new FlinkKafkaProducer[InternalEvent](
    "input-adapter-out", new InternalEventSerializer(), params.kafkaProducerProperties
  )

  InputAdapterBuilder(
    env = env,
    stringEventSource = stringEventConsumer,
    internalEventSink = eventProducer
  ).build()

  env.execute()
}
