package ru.star

import java.util.Properties

import scala.collection.JavaConverters._

final case class Parameters(kafkaConsumerProperties: Properties,
                            kafkaProducerProperties: Properties) {

}

object Parameters {
  val BootstrapServers = "bootstrap.servers"

  object Default {
    val BootstrapServers = "localhost:9092"
  }

  def apply(args: Array[String]): Parameters = {

    val kafkaConsumerPropertyMap = Map.empty[String, String]

    //    args.sliding(2, 2).toList.collect {
    //      case Array("--ip", argIP: String) => ip = argIP
    //      case Array("--port", argPort: String) => port = argPort.toInt
    //      case Array("--name", argName: String) => name = argName
    //    }
    val kafkaConsumerProperties = new Properties()
    kafkaConsumerProperties.putAll(kafkaConsumerPropertyMap.asJava)

    new Parameters(kafkaConsumerProperties, kafkaConsumerProperties)
  }
}
