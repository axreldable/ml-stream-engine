package ru.star

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

object CoreJob extends App {
  println("CoreJob started.")

  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val params: Parameters = Parameters(args)

  val eventConsumer = new FlinkKafkaConsumer[InternalEvent](
    "input-event-out", new InternalEventDeserializer(), params.kafkaConsumerProperties)

  val configConsumer = new FlinkKafkaConsumer[InternalConfig](
    "input-config-out", new InternalConfigDeserializer(), params.kafkaConsumerProperties)

  val modelConsumer = new FlinkKafkaConsumer[InternalModel](
    "input-model-out", new InternalModelDeserializer(), params.kafkaConsumerProperties)

  val predictionProducer = new FlinkKafkaProducer[ConfiguredEventWithPrediction](
    "output-event-in", new ConfiguredEventWithPredictionSerializer(), params.kafkaProducerProperties
  )

  MLExecutorBuilder(
    env = env,
    eventSource = eventConsumer,
    configSource = configConsumer,
    modelSource = modelConsumer,
    eventWithPredictionSink = predictionProducer
  ).build()

  env.execute()
}
