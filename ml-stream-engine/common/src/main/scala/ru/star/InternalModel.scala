package ru.star

import io.radicalbit.flink.pmml.scala.api.reader.ModelReader

final case class InternalModel(id: String,
                               modelReader: ModelReader)
