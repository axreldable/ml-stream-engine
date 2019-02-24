package ru.star

import org.scalatest._

class ParametersSpec extends FlatSpec with Matchers {
  behavior of "Parameters"

  it should "parse command line arguments, extract needed values and skip not needed" in {
    val args = "--bootstrap.servers localhost:8888 --key value".split(" ")

    val parameters = Parameters(args)

    parameters.kafkaConsumerProperties.getProperty("bootstrap.servers") shouldBe "localhost:8888"
    Option(parameters.kafkaProducerProperties.getProperty("key")) shouldBe empty
  }

  it should "use default values if needed arguments is absent" in {
    val args = Array.empty[String]

    val parameters = Parameters(args)

    parameters.kafkaConsumerProperties.getProperty("bootstrap.servers") shouldBe "localhost:9092"
  }
}
