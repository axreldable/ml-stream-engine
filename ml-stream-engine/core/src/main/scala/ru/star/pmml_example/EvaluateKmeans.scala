package ru.star.pmml_example

import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.streaming.api.datastream.DataStreamUtils
import org.apache.flink.streaming.api.scala._

object EvaluateKmeans {

  def main(args: Array[String]): Unit = {
    val params: ParameterTool = ParameterTool.fromArgs(args)
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment

    val inputModel = "./pmml_source/fraud_detection_5.pmml"
    val output = "/Users/user/Desktop/msu/msu-diploma-thesis/ml-stream-engine"

    val fraudDataStream: DataStream[FraudElement] = env.fromCollection(List(
      FraudElement(
        Array(
          162065.000000,
          0.171839,
          0.962726,
          -0.793596,
          -0.888453,
          1.450265,
          0.049527,
          0.856367,
          0.095876,
          -0.247813,
          -0.665789,
          0.137794,
          0.273099,
          0.144056,
          -0.889463,
          -0.873351,
          0.812298,
          -0.244629,
          0.594006,
          0.430576,
          0.066033,
          -0.353719,
          -0.899193,
          -0.037913,
          -0.452444,
          -0.318070,
          0.155473,
          0.217415,
          0.064375,
          3.590000
        )
      )
//      FraudElement(
//        Array(
//          52086.000000,
//          1.393699,
//          -1.032538,
//          0.831573,
//          -1.236124,
//          -1.809026,
//          -1.056613,
//          -0.928571,
//          -0.277339,
//          -1.951564,
//          1.357031,
//          0.072154,
//          -0.086470,
//          1.698049,
//          -0.593138,
//          0.603027,
//          -0.334686,
//          0.475585,
//          -0.330368,
//          -0.384722,
//          -0.164900,
//          -0.306335,
//          -0.574792,
//          0.165954,
//          0.701641,
//          0.112771,
//          -0.439452,
//          0.042931,
//          0.043623,
//          58.000000
//        )
//      )
    ))

    //Load model
    val modelReader: ModelReader = ModelReader(inputModel)

    //Using evaluate operator
    val prediction: DataStream[(FraudElement, Double)] = fraudDataStream.evaluate(modelReader) {
      //Iris data and modelReader instance
      case (event, model) =>
        val vectorized = DenseVector(event.vArray)
        val prediction = model.predict(vectorized)
        (event, prediction.value.get)
    }

    //    prediction.javaStream.print()

    val javaStream = DataStreamUtils.collect(prediction.javaStream)
    while (javaStream.hasNext) {
      val r = javaStream.next()
      val ev = r._1.vArray.mkString(", ")
      val pred = r._2
      println(ev, pred)
    }

    env.execute("Clustering example")
  }
}
