import sbt._

object Dependencies {
  lazy val flinkVersion = "1.7.1"

  lazy val flinkCore = "org.apache.flink" % "flink-core" % flinkVersion withSources() withJavadoc()
  lazy val flinkStreaming = "org.apache.flink" %% "flink-streaming-scala" % flinkVersion withSources() withJavadoc()
  lazy val flinkConnectorKafka = "org.apache.flink" %% "flink-connector-kafka" % flinkVersion withSources() withJavadoc()
  lazy val flinkJpmml = "io.radicalbit" %% "flink-jpmml-scala" % "0.6.3" withSources() withJavadoc()
  lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.10.2"
  
  // fix: SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
  lazy val slf4jHelper = "org.slf4j" % "slf4j-jdk14" % "1.7.15"


  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

  lazy val sparkVersion = "2.4.3"
  lazy val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion
  lazy val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion
  lazy val scopt = "com.github.scopt" %% "scopt" % "3.7.0"
  lazy val sparkTestingBase = "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_0.12.0"
}
