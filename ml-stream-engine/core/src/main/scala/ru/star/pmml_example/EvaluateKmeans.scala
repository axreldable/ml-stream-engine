package ru.star.pmml_example

import io.radicalbit.flink.pmml.scala._
import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._

object EvaluateKmeans {

  def main(args: Array[String]): Unit = {
    val params: ParameterTool = ParameterTool.fromArgs(args)
    implicit val env = StreamExecutionEnvironment.getExecutionEnvironment

    val inputModel = "path-to-pmml"
    val output = "path-to-output"

    //Read data from custom iris source
    val irisDataStream = IrisSource.irisSource(env, None)

    //Load model
    val modelReader: ModelReader = ModelReader(inputModel)

    //Using evaluate operator
    val prediction = irisDataStream.evaluate(modelReader) {
      //Iris data and modelReader instance
      case (event, model) =>
        val vectorized = event.toVector
        val prediction = model.predict(vectorized, Some(0.0))
        (event, prediction.value.getOrElse(-1.0))
    }

    prediction.writeAsText(output)

    env.execute("Clustering example")
  }
}
