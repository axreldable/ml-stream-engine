package ru.star

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object CoreJob extends App {
  println("CoreJob started.")

  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val params: Parameters = Parameters(args)

  val eventConsumer = new FlinkKafkaConsumer[InternalEvent](
    "input-adapter-out", new InternalEventDeserializer(), params.kafkaConsumerProperties)

  MLExecutorBuilder(
    env = env,
    eventSource = eventConsumer
  ).build()

  env.execute()
}
