package ru.star

import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

final case class MessageWorkerBuilder(env: StreamExecutionEnvironment,
                                      eventSink: SinkFunction[String],
                                      configSink: SinkFunction[String],
                                      modelSink: SinkFunction[String],
                                      alarmSource: SourceFunction[String],
                                      otherSource: SourceFunction[String]) {
  def build(): Unit = {
    val stringEvents = env.readTextFile("./sources/events.txt")
    val stringConfig = env.readTextFile("./sources/config.txt")
    val stringModels = env.readTextFile("./sources/models.txt")

  }
}