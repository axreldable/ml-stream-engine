package ru.star

import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkMlJob extends App {
  val appName: String = "legacy-streaming-example"
  val spark: SparkSession = SparkSession.builder()
    .appName(appName)
    .master("local[2]")
    .config("spark.driver.memory", "1g")
    .getOrCreate()
  spark.sparkContext.setLogLevel("WARN")


  val inputStreamPath = getClass.getClassLoader.getResource("events-stream").getPath

  val dataSchema = new StructType()
    .add("tweet", StringType)

  val inputDF = spark
    .readStream
    .schema(dataSchema)
    .option("maxFilesPerTrigger", 1)
    .json(inputStreamPath)

  inputDF.writeStream.foreachBatch { (batchDF: DataFrame, batchId: Long) =>
    batchDF
      .show()
  }.start()

  spark.streams.awaitAnyTermination()
}
