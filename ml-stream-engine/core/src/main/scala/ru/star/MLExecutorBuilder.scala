package ru.star

import io.radicalbit.flink.pmml.scala.api.reader.ModelReader
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import ru.star.functions._

final case class MLExecutorBuilder(env: StreamExecutionEnvironment,
                                   eventSource: SourceFunction[InternalEvent],
                                   configSource: SourceFunction[InternalConfig],
                                   modelSource: SourceFunction[InternalModel],
                                   eventSink: SinkFunction[InternalEvent]
                                  ) {

  def build(): Unit = {
    val configs = env.addSource(configSource)

    val models = env.addSource(modelSource)

    val events: DataStream[InternalEvent] = env.addSource(eventSource)

    val configuringEvents: DataStream[ConfiguringEvent] = events
      .connect(configs)
      .flatMap(new ConfigureEventFunction())

    val eventModelPair: DataStream[(ConfiguringEvent, InternalModel)] = configuringEvents
      .connect(models)
      .flatMap(new EventModelJoiner())

//    val in = new FileInputStream("")
//    val source = ImportFilter.apply(new InputSource(in))
//    val pmml = JAXBUtil.unmarshalPMML(source)
//    val pmmlManager = new PMMLManager( pmml )




    //    val modelReader: ModelReader = eventModelPair._2.modelReader
    val modelReader: ModelReader = ModelReader("")


    //Using evaluate operator
    val prediction: DataStream[(InternalEvent, Double)] = events.evaluate(modelReader) {
      //Iris data and modelReader instance
      case (event, model) =>
        val vectorized = DenseVector(event.message.get.split(" ").map(_.toDouble))
        val prediction = model.predict(vectorized)
        (event, prediction.value.get)
    }

    eventModelPair.process(new PredictProcessFunction())





    // we need connect config event and model here


    env
      .addSource(eventSource)
      .map(event => {
        println("In core " + event)
        event
      })
      .addSink(eventSink)
  }

//  import org.dmg.pmml.PMML
//  import org.jpmml.model.ImportFilter
//  import org.jpmml.model.JAXBUtil
//  import javax.xml.transform.Source
//  import java.io.FileInputStream
//  import java.io.InputStream
//
//  @throws[Exception]
//  def loadModel(file: String): PMML = {
//    var pmml = null
//    val inputFilePath = new Nothing(file)
//    try {
//      val in = new FileInputStream(inputFilePath)
//      try {
//        val source = ImportFilter.apply(new Nothing(in))
//        pmml = JAXBUtil.unmarshalPMML(source)
//      } catch {
//        case e: Exception =>
//          logger.error(e.toString)
//          throw e
//      } finally if (in != null) in.close()
//    }
//    pmml
//  }
}
