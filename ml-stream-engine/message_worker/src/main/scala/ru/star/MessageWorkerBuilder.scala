package ru.star

import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

import scala.io.Source

final case class MessageWorkerBuilder(env: StreamExecutionEnvironment,
                                      eventSink: SinkFunction[String],
                                      configSink: SinkFunction[String],
                                      modelSink: SinkFunction[String],
                                      alarmSource: SourceFunction[String],
                                      otherSource: SourceFunction[String]) {
  def build(): Unit = {
    val stringEvents = env.readTextFile("./sources/events.txt")
    val stringConfig = env.fromCollection(List(Source.fromFile("./sources/config.conf").mkString))
    val stringModels = env.readTextFile("./sources/models.txt")

    rout(stringEvents, eventSink)
    rout(stringConfig, configSink)
    rout(stringModels, modelSink)
  }

  def rout(from: DataStream[String], to: SinkFunction[String]): Unit = {
    from.map(message => {
      //        println(message)
      message
    }).addSink(to)
  }
}