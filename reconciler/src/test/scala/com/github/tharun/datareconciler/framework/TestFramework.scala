package com.github.tharun.datareconciler.framework

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.scalatest.FlatSpec

abstract class TestFramework extends FlatSpec {

  val spark = SparkSession.builder()
    .master("local[*]")
    .appName("Test Suite")
    .config("spark.ui.enabled", value = false)
    .config("spark.driver.bindAddress", value = "127.0.0.1")
    .config("spark.executor.memory", "2g")
    .config("spark.driver.host", "localhost")
    // unit test optimizations
    // https://spark.apache.org/docs/latest/configuration.html#available-properties
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .config("spark.kryo.unsafe", "true")
    .config("spark.kryoserializer.buffer", "64k")
    .config("spark.serializer.objectStreamReset", "-1")
    .config("spark.broadcast.compress", "false")
    .config("spark.rdd.compress", "false")
    .config("spark.shuffle.compress", "false")
    // spark sql tweaks
    .config("spark.sql.inMemoryColumnarStorage.compressed", "false")
    .config("spark.sql.optimizer.metadataOnly ", "false")
    .config("spark.sql.inMemoryColumnarStorage.compressed ", "false")
    .config("spark.sql.orc.compression.codec ", "false")
    .config("spark.sql.shuffle.partitions", "4")
    // this is set so that T_L2_PAT_PROC doesn't take a horrendously long time. 28 seems to be a sweet spot.
    .config("spark.sql.optimizer.maxIterations", "28")
    .config("spark.debug.maxToStringFields", "200")
    .getOrCreate()

  val logClass = Seq("org.apache.spark", "org.apache.hadoop", "org.apache.parquet", "org.spark_project", "org.eclipse.jetty", "akka", "io.netty")

  for (c <- logClass ){
    val logger: Logger = Logger.getLogger(c)
    logger.setLevel(Level.WARN)
  }

}
