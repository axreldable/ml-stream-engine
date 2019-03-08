package ru.star

import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import io.radicalbit.flink.pmml.scala.models.input.BaseEvent
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}

object SimpleCheck extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  println("Start.")

  val eventStream = env.fromCollection(
    List(Event(1, 1, 2), Event(2, 1, 1), Event(3, 1, 2))
  )

  eventStream.map(pred => {
    println(pred)
    pred
  })

  val modelReader: ModelReader = ModelReader("./pmml_source/simple.pmml")

  //Using evaluate operator
  val prediction: DataStream[(Event, Double)] = eventStream.evaluate(modelReader) {
    //Iris data and modelReader instance
    case (event, model) =>
      val vectorized = DenseVector(Array(event.x1, event.x2, event.x3))
      val prediction = model.predict(vectorized)
      (event, prediction.value.get)
  }

  prediction.map(pred => {
    println(pred)
    pred
  })

  env.execute()
  println("Finish.")
}

final case class Event(x1: Int, x2: Int, x3: Int) extends BaseEvent {
  override def modelId: String = "1"

  override def occurredOn: Long = 1
}
