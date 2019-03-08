package ru.star

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

object CoreJob extends App {
  println("CoreJob started.")

  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val params: Parameters = Parameters(args)

  val eventConsumer = new FlinkKafkaConsumer[InternalEvent](
    "input-adapter-out", new InternalEventDeserializer(), params.kafkaConsumerProperties)

  val eventProducer = new FlinkKafkaProducer[InternalEvent](
    "output-adapter-in", new InternalEventSerializer(), params.kafkaProducerProperties
  )

//  MLExecutorBuilder(
//    env = env,
//    eventSource = eventConsumer,
//    eventSink = eventProducer
//  ).build()

  env.execute()
}
