package ru.star

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

object OutputAdapterJob extends App {
  println("output-adapter started.")

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val params: Parameters = Parameters(args)

  val eventConsumer = new FlinkKafkaConsumer[InternalEvent](
    "output-event-in", new InternalEventDeserializer(), params.kafkaConsumerProperties
  )

  val alarmProducer = new FlinkKafkaProducer[String](
    "output-alarm-topic", new SimpleStringSchema(), params.kafkaProducerProperties
  )

  val otherProducer = new FlinkKafkaProducer[String](
    "output-other-topic", new SimpleStringSchema(), params.kafkaProducerProperties
  )

  OutputAdapterBuilder(
    env = env,
    eventSource = eventConsumer,
    stringSink = stringProducer
  ).build()

  env.execute()
}
