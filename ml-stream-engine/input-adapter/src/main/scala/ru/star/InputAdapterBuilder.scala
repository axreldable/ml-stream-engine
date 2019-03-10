package ru.star

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

final case class InputAdapterBuilder(env: StreamExecutionEnvironment,
                                     eventSource: SourceFunction[String],
                                     configSource: SourceFunction[String],
                                     modelSource: SourceFunction[String],
                                     eventSink: SinkFunction[InternalEvent],
                                     configSink: SinkFunction[InternalConfig],
                                     modelSink: SinkFunction[InternalModel]
                                    ) {
  def build(): Unit = {
    processMessage(eventSource, eventSink, InternalEvent.fromString)
    processMessage(configSource, configSink, InternalConfig.fromString)
    processMessage(modelSource, modelSink, InternalModel.fromString)
  }

  def processMessage[IN: TypeInformation, OUT: TypeInformation](source: SourceFunction[IN],
                                                                sink: SinkFunction[OUT],
                                                                f: IN => OUT): Unit = {
    env
      .addSource(source)
      .map(message => {
        println(s"Precessing '$message' in input-adapter.")
        f(message)
      })
      .addSink(sink)
  }
}
