package ru.star

import java.time.LocalDateTime
import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object CoreJob extends App with LazyLogging {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  println("Start.")
  val params: Parameters = Parameters(args)
  params.kafkaConsumerProperties

  val eventConsumer = FlinkKafkaConsumer[InternalEvent]("test", new SimpleStringSchema(), params.kafkaConsumerProperties)

//  MLExecutorBuilder(
//    events =
//  )



//  val stream: DataStream[InternalEvent] = env
//    .addSource(new FlinkKafkaConsumer[String]("test", new SimpleStringSchema(), properties))
//    .map(mes => {
//      val event = InternalEvent(LocalDateTime.now().toString, mes)
//      println(event)
//      event
//    })
//
//  env.execute()
}
