package ru.star.functions

import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import org.apache.flink.api.common.functions.MapFunction
import ru.star.InternalEvent

final case class PredictWithPmmlFunction(modelReaders: List[ModelReader])
  extends MapFunction[ConfigureEventFunction, (InternalEvent, Double)] {
  override def map(value: ConfigureEventFunction): (InternalEvent, Double) = ???
}
