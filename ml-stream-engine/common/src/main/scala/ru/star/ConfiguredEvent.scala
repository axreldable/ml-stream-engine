package ru.star

import io.radicalbit.flink.pmml.scala.models.input.BaseEvent

final case class ConfiguredEvent(id: String, event: InternalEvent, config: EventConfig)
  extends Serializable with BaseEvent {
  override def modelId: String = s"${config.modelConfig.name}_${config.modelConfig.name}"

  override def occurredOn: Long = System.currentTimeMillis()
}