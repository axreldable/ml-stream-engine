package ru.star

import org.apache.flink.streaming.api.scala.DataStream

final case class MLExecutorBuilder(events: DataStream[InternalEvent],
                                   models: DataStream[PmmlModel]) {

}
