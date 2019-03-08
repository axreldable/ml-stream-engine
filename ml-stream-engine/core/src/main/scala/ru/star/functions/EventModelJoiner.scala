package ru.star.functions

import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction
import org.apache.flink.streaming.api.scala.createTypeInformation
import org.apache.flink.util.Collector
import ru.star.{ConfiguringEvent, InternalModel}

class EventModelJoiner
  extends RichCoFlatMapFunction[ConfiguringEvent, InternalModel, (ConfiguringEvent, InternalModel)] {
  private var models: MapState[String, InternalModel] = _

  override def flatMap1(inputEvent: ConfiguringEvent, out: Collector[(ConfiguringEvent, InternalModel)]): Unit = {
    println(s"ConfiguringEvent='$inputEvent' received.")

    out.collect((inputEvent, models.get(inputEvent.modelId)))
  }

  override def flatMap2(inputModel: InternalModel, out: Collector[(ConfiguringEvent, InternalModel)]): Unit = {
    println(s"New model '$inputModel' received.")

    models.put(inputModel.id, inputModel)
  }

  override def open(parameters: Configuration): Unit = {
    models = getRuntimeContext.getMapState(
      new MapStateDescriptor("config", createTypeInformation[String], createTypeInformation[InternalModel])
    )
  }
}
