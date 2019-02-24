package ru.star

import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

final case class OutputAdapterBuilder(env: StreamExecutionEnvironment,
                                      eventSource: SourceFunction[InternalEvent],
                                      stringSink: SinkFunction[String]) {
  def build(): Unit = {
    env
      .addSource(eventSource)
      .map(event => {
        println("In output adapter " + event)
        InternalEvent.toString(event)
      })
      .addSink(stringSink)
  }
}
