package ru.star

import java.nio.file.Paths

import pureconfig.ConfigFieldMapping
import pureconfig.generic.ProductHint
// don't remove for avoid pureconfig errors: import pureconfig.generic.auto._
import pureconfig.generic.auto._
import pureconfig._

final case class VerySimple(id: String)

case class SampleConf(foo: Int, bar: String)

object CheckConfig extends App {
  implicit def forReadSnakeCaseInConfig[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, SnakeCase))

  val config = loadConfigOrThrow[InternalConfig](Paths.get("./sources/config.conf"))

  println(config)
}
