name := "ml-stream-engine"
organization in ThisBuild := "ru.star"
scalaVersion in ThisBuild := "2.11.12"
parallelExecution in ThisBuild := false
logLevel := Level.Warn

lazy val global = project
  .in(file("."))
  .settings(version := "1.0.0")
  .aggregate(
    input_adapter,
    generator,
    reader,
    spark_tweet_job,
    output_adapter,
    pmml_job,
    common,
    service_ml_job
  )

lazy val input_adapter = project
  .in(file("input-adapter"))
  .settings(
    name := "input-adapter",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.pureConfig,
      Dependencies.flinkCore,
      Dependencies.flinkStreaming,
      Dependencies.flinkConnectorKafka,
      Dependencies.typesafeLogging
    )
  ).dependsOn(common)

lazy val common = project
  .in(file("common"))
  .settings(
    name := "common",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.flinkCore,
      Dependencies.flinkStreaming,
      Dependencies.flinkPmml,
      Dependencies.slf4j,
      Dependencies.slf4j12,
      Dependencies.pureConfig,
    )
  )

lazy val output_adapter = project
  .in(file("output-adapter"))
  .settings(
    name := "output-adapter",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.pureConfig,
      Dependencies.flinkCore,
      Dependencies.flinkStreaming,
      Dependencies.flinkConnectorKafka,
      Dependencies.typesafeLogging
    )
  ).dependsOn(common)

lazy val generator = project
  .in(file("generator"))
  .settings(
    name := "generator",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.sparkCore % Provided,
      Dependencies.sparkSql % Provided,
      Dependencies.kafkaClients,
    )
  ).dependsOn(common)

lazy val reader = project
  .in(file("reader"))
  .settings(
    name := "reader",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.sparkCore % Provided,
      Dependencies.sparkSql % Provided,
      Dependencies.kafkaClients,
      Dependencies.logback,
      Dependencies.typesafeLogging,
    )
  )

lazy val spark_tweet_job = project
  .in(file("spark-tweet-job"))
  .settings(
    name := "spark-tweet-job",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.sparkCore,
      Dependencies.sparkSql,
      Dependencies.sparkStreaming,
      Dependencies.sparkStreamingKafka,
      Dependencies.sparkMlLib,
      Dependencies.kafkaClients,
      Dependencies.logback,
      Dependencies.typesafeLogging,
      Dependencies.pureConfig,
    )
  )

lazy val pmml_job = project
  .in(file("pmml-job"))
  .settings(
    name := "pmml-job",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.flinkConnectorKafka,
    )
  ).dependsOn(common)

lazy val service_ml_job = project
  .in(file("service-ml-job"))
  .settings(
    name := "service-ml-job",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.kafkaStreams,
      Dependencies.grpcNetty,
      Dependencies.grpcAll,
      Dependencies.grpcProtobufJava,
    )
  ).dependsOn(common)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + "_" + version.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case _ => MergeStrategy.first
  }
)
