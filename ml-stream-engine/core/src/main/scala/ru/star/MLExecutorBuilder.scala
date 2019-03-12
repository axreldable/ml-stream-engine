package ru.star

import java.util.UUID

import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import ru.star.functions._

final case class MLExecutorBuilder(env: StreamExecutionEnvironment,
                                   eventSource: SourceFunction[InternalEvent],
                                   configSource: SourceFunction[InternalConfig],
                                   modelSource: SourceFunction[InternalModel],
                                   eventWithPredictionSink: SinkFunction[ConfiguredEventWithPrediction]
                                  ) {

  def build(): Unit = {
    val events = env.addSource(eventSource).keyBy(_.key)
    val configs = env.addSource(configSource).keyBy(_.key)

    val configuredEvents =
      events.connect(configs)
        .flatMap(new ConfigureEventFunction())

    val models = env.addSource(modelSource).map(_.toAddMessage)

    val configuredEventsWithPredictions = configuredEvents.withSupportStream(models).evaluate {
      (configuredEvent, model) =>
        val vector = EventTransformer.transform(
          configuredEvent.event, configuredEvent.config.transformConfig.funcName
        )
        val prediction = model.predict(vector)

        ConfiguredEventWithPrediction(
          id = UUID.randomUUID().toString,
          configuredEvent = configuredEvent,
          predictedValue = prediction.value.get
        )
    }

    configuredEventsWithPredictions.map(event => {
      println(event)
      event
    })
  }
}
