import sbt._

object Dependencies {
  lazy val sparkVersion = "2.4.0"

  lazy val sparkCore = "org.apache.spark" %% "spark-core" % sparkVersion
  lazy val sparkSql = "org.apache.spark" %% "spark-sql" % sparkVersion
}
