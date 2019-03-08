package ru.star.functions

import io.radicalbit.flink.pmml.scala.api.PmmlModel
import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.util.Collector
import ru.star.pmml_example.FraudElement
import ru.star.{ConfiguringEvent, EventWithPrediction, InternalModel}

class PredictProcessFunction() extends ProcessFunction[(ConfiguringEvent, InternalModel), EventWithPrediction] {
  override def processElement(eventModelPair: (ConfiguringEvent, InternalModel),
                              ctx: ProcessFunction[(ConfiguringEvent, InternalModel), EventWithPrediction]#Context,
                              out: Collector[EventWithPrediction]): Unit = {

//    val modelReader: ModelReader = eventModelPair._2.modelReader
//
//    //Using evaluate operator
//    val prediction: DataStream[(FraudElement, Double)] = fraudDataStream.evaluate(modelReader) {
//      //Iris data and modelReader instance
//      case (event, model) =>
//        val vectorized = DenseVector(event.vArray)
//        val prediction = model.predict(vectorized)
//        (event, prediction.value.get)
//    }
  }
}
