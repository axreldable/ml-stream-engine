package ru.star.functions

import java.util.UUID

import org.apache.flink.api.common.state._
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction
import org.apache.flink.streaming.api.scala.createTypeInformation
import org.apache.flink.util.Collector
import ru.star.{ConfiguredEvent, InternalConfig, InternalEvent}

class ConfigureEventFunction extends RichCoFlatMapFunction[InternalEvent, InternalConfig, ConfiguredEvent] {
  private var currentConfig: ValueState[InternalConfig] = _

  override def flatMap1(inputEvent: InternalEvent, out: Collector[ConfiguredEvent]): Unit = {
    println(s"InternalEvent='$inputEvent' received.")

    currentConfig.value().eventConfigs.get(inputEvent.configName) match {
      case Some(eventConfig) =>
        println(s"Creating ConfiguredEvent from InternalEvent='$inputEvent' and EventConfig='$eventConfig'.")
        out.collect(ConfiguredEvent(
          UUID.randomUUID().toString, inputEvent, eventConfig
        ))
      case None =>
        println(s"Fail to find config for InternalEvent='$inputEvent' in Config='${currentConfig.value()}'.")
    }
  }

  override def flatMap2(inputConfig: InternalConfig, out: Collector[ConfiguredEvent]): Unit = {
    println(s"Config updated to '$inputConfig'.")

    currentConfig.update(inputConfig)
  }

  override def open(parameters: Configuration): Unit = {
    currentConfig = getRuntimeContext.getState(
      new ValueStateDescriptor("config", createTypeInformation[InternalConfig])
    )
  }
}
