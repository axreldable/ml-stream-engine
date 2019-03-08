package ru.star.functions

import org.apache.flink.api.common.state._
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction
import org.apache.flink.streaming.api.scala.createTypeInformation
import org.apache.flink.util.Collector
import ru.star.{ConfiguringEvent, InternalConfig, InternalEvent}

class ConfigureEventFunction extends RichCoFlatMapFunction[InternalEvent, InternalConfig, ConfiguringEvent] {
  private var currentConfig: ValueState[InternalConfig] = _

  override def flatMap1(inputEvent: InternalEvent, out: Collector[ConfiguringEvent]): Unit = {
    println(s"InternalEvent='$inputEvent' received.")

    currentConfig.value().eventModelMap.get(inputEvent.id) match {
      case Some(modelId) =>
        println(s"Creating ConfiguringEvent from InternalEvent='$inputEvent' and modelId='$modelId'.")
        out.collect(ConfiguringEvent(modelId, inputEvent))
      case None =>
        println(s"Fail to find model for InternalEvent='$inputEvent' in Config='${currentConfig.value()}'.")
    }
  }

  override def flatMap2(inputConfig: InternalConfig, out: Collector[ConfiguringEvent]): Unit = {
    println(s"Config updated to '$inputConfig'.")

    currentConfig.update(inputConfig)
  }

  override def open(parameters: Configuration): Unit = {
    currentConfig = getRuntimeContext.getState(
      new ValueStateDescriptor("config", createTypeInformation[InternalConfig])
    )
  }
}
