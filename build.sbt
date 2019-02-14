name := "ml-stream-engine"
organization in ThisBuild := "ru.star"
scalaVersion := "2.12.8"
logLevel := Level.Info

lazy val global = project
  .in(file("."))
  .settings(version := "1.0.0")
  .aggregate(
    common,
    core,
    input_adapter,
    output_adapter,
  )

lazy val common = project
  .in(file("common"))
  .settings(
    name := "common",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.flinkCore,
      Dependencies.flinkStreaming,
      Dependencies.flinkConnectorKafka,
    )
  )

lazy val input_adapter = project
  .in(file("input-adapter"))
  .settings(
    name := "input-adapter",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq()
  ).dependsOn(common)

lazy val output_adapter = project
  .in(file("output-adapter"))
  .settings(
    name := "output-adapter",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq()
  ).dependsOn(common)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "core",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq()
  ).dependsOn(common)

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + "_" + version.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case _                           => MergeStrategy.first
  }
)