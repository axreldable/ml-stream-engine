import sbt._

object Dependencies {
  lazy val flinkVersion = "1.7.1"

  lazy val flinkCore = "org.apache.flink" % "flink-core" % flinkVersion
  lazy val flinkStreaming = "org.apache.flink" %% "flink-streaming-scala" % flinkVersion
  lazy val flinkConnectorKafka = "org.apache.flink" %% "flink-connector-kafka" % flinkVersion
  lazy val flinkJpmml = "io.radicalbit" %% "flink-jpmml-scala" % "0.6.3" withSources() withJavadoc()

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
}
