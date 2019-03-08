package ru.star

import java.util.Properties

import org.apache.flink.api.java.utils.ParameterTool

final case class Parameters(kafkaConsumerProperties: Properties,
                            kafkaProducerProperties: Properties)

object Parameters {
  val BootstrapServers = "bootstrap.servers"

  object Default {
    val BootstrapServers = "localhost:9092"
  }

  def apply(args: Array[String]): Parameters = {
    println(s"Starting parsing arguments='${args.mkString(" ")}'")

//     todo: replace it with  ParameterTool.fromArgs(args)
//    https://ci.apache.org/projects/flink/flink-docs-release-1.7/dev/best_practices.html
    val argsMap = args.sliding(2, 2)
      .map(pair => (pair(0).substring(2), pair(1)))
      .foldLeft(Map.empty[String, String])({
        case (map, (key, value)) => map + (key -> value)
      })

    val bootstrapServers = argsMap.getOrElse(BootstrapServers, {
      println(s"Fail to extract $BootstrapServers from arguments. Will use ${Default.BootstrapServers}")
      Default.BootstrapServers
    })

    val kafkaConsumerProperties = new Properties()
    kafkaConsumerProperties.put(BootstrapServers, bootstrapServers)

    val kafkaProducerProperties = new Properties()
    kafkaProducerProperties.put(BootstrapServers, bootstrapServers)

    new Parameters(kafkaConsumerProperties, kafkaProducerProperties)
  }
}
