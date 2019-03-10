package ru.star.functions

import org.apache.flink.ml.math.DenseVector
import ru.star.InternalEvent

object EventTransformer {
  def transform(event: InternalEvent, funcName: String): DenseVector = {
    funcName match {
      case "fraudTransform" => fraudTransform(event)
    }
  }

  def fraudTransform(event: InternalEvent): DenseVector = {
    val ar = event.message.split(" ")

    DenseVector(Array(ar(0), ar(1), ar(2)).map(_.toDouble))
  }
}
