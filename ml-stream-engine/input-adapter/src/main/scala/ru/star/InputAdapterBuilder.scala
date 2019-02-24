package ru.star

import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

final case class InputAdapterBuilder(env: StreamExecutionEnvironment,
                                     stringEventSource: SourceFunction[String],
                                     internalEventSink: SinkFunction[InternalEvent]) {
  def build(): Unit = {
    env
      .addSource(stringEventSource)
      .map(stringEvent => {
        println("In input-adapter " + stringEvent)
        InternalEvent.fromString(stringEvent)
      })
      .addSink(internalEventSink)
  }
}
