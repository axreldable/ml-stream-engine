package ru.star

import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import io.radicalbit.flink.pmml.scala.models.control.{AddMessage, ServingMessage}
import io.radicalbit.flink.pmml.scala.models.input.BaseEvent
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}

object SimpleCheck1 extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  println("Start.")

  val eventStream = env.fromCollection(
    List(Event1(1, 1, 2), Event1(2, 1, 1), Event1(3, 1, 2))
  )

  eventStream.map(pred => {
    println(pred)
    pred
  })



  val controlStream: DataStream[ServingMessage] = env.fromCollection(
    List(AddMessage("name", 2, "./pmml_source/simple.pmml", 1))
  )

  val predictions =
    eventStream
      .withSupportStream(controlStream)
      .evaluate { (event, model) =>
        val vector = DenseVector(Array(event.x1, event.x2, event.x3))
        val prediction = model.predict(vector)

        prediction
      }

  predictions.map(pred => {
    println(pred)
    pred
  })

  env.execute()
  println("Finish.")
}

final case class Event1(x1: Int, x2: Int, x3: Int) extends BaseEvent {
  override def modelId: String = "model_1"

  override def occurredOn: Long = 1
}
