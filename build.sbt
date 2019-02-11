name := "ml-stream-engine"
organization in ThisBuild := "ru.star"
scalaVersion := "2.12.8"
logLevel := Level.Info

lazy val global = project
  .in(file("."))
  .settings(version := "1.0.0")
  .aggregate(
    input_adapter
  )

lazy val input_adapter = project
  .in(file("input-adapter"))
  .settings(
    name := "input-adapter",
    version := "1.0.0",
    assemblySettings,
    libraryDependencies ++= Seq(
      Dependencies.sparkCore,
    )
  )

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + "_" + version.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case _                           => MergeStrategy.first
  }
)