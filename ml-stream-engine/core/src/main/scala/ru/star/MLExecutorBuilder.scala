package ru.star

import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

final case class MLExecutorBuilder(env: StreamExecutionEnvironment,
                                   eventSource: SourceFunction[InternalEvent],
                                   //                                   config: DataStream[String],
                                   //                                   models: DataStream[String]
                                   eventSink: SinkFunction[InternalEvent]
                                  ) {

  def build(): Unit = {
    env
      .addSource(eventSource)
      .map(event => {
        println("In core " + event)
        event
      })
      .addSink(eventSink)
  }
}
