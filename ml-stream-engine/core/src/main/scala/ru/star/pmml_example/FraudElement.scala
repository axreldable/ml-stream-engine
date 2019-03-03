package ru.star.pmml_example

import io.radicalbit.flink.pmml.scala.models.input.BaseEvent

import scala.util.Random

final case class FraudElement(vArray: Array[Double]) extends BaseEvent {
  override def modelId: String = Random.nextInt(100).toString

  override def occurredOn: Long = System.currentTimeMillis()
}

