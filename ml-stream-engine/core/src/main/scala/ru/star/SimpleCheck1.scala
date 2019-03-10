package ru.star

import java.util.UUID

import io.radicalbit.flink.pmml.scala.models.control.{AddMessage, ServingMessage}
import io.radicalbit.flink.pmml.scala.models.input.BaseEvent
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.dmg.pmml.{Model, PMML}
import org.jpmml.evaluator.{ModelEvaluator, ModelEvaluatorFactory}
import org.jpmml.model.JAXBUtil

object SimpleCheck1 extends App {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.enableCheckpointing(5000)
  println("Start.")

  val eventStream = env.fromCollection(
    List(Event1(1, 1, 2), Event1(2, 1, 1), Event1(3, 1, 2))
  )

  val controlStream: DataStream[ServingMessage] = env.fromCollection(
    List(
      AddMessage("c056b84c-6c58-42b7-b98e-90da537995a2", 1, "./pmml_source/simple.pmml", System.currentTimeMillis())
    )
  )

  val eventWithControl = eventStream.withSupportStream(controlStream)

  val predictions =
    eventWithControl
      .evaluate { (event, model) =>
        val vector = DenseVector(Array(event.x1, event.x2, event.x3))
        val prediction = model.predict(vector)

        prediction
      }

  predictions.map(pred => {
    println(pred)
    pred
  })

//  val eventStream1 = env.fromCollection(
//    List(Event1(1, 1, 2), Event1(2, 1, 1), Event1(3, 1, 2))
//  )
//
//  eventStream1.withSupportStream(controlStream)
//    .evaluate { (event, model) =>
//      val vector = DenseVector(Array(event.x1, event.x2, event.x3))
//      val prediction = model.predict(vector)
//
//      prediction
//    }.map(pred => {
//    println(pred)
//    pred
//  })

//  val pmml: PMML = org.jpmml.model.PMMLUtil.unmarshal(is)
//  val modelEvaluatorBuilder = new ModelEvaluatorBuilder(pmml)


  env.execute()
  println("Finish.")
}

final case class Event1(x1: Int, x2: Int, x3: Int) extends BaseEvent {
  override def modelId: String = "c056b84c-6c58-42b7-b98e-90da537995a2_1"

  override def occurredOn: Long = 1
}
