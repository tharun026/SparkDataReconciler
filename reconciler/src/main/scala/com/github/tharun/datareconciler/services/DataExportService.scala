package com.github.tharun.datareconciler.services

import org.apache.spark.sql.{DataFrame, SparkSession}

class DataExportService (outputPath: String, spark: SparkSession) {

  private def getS3StoreUrl(tableName: String): String = {
    outputPath + tableName
  }

  def exportToS3AndJSON(df: DataFrame, tableName: String, partitionColumns: Seq[String], partitionCount: Int): Unit = {
    exportToS3(df, tableName, partitionColumns, partitionCount)
    exportToJSON(df, tableName)
  }

  def exportToS3AndJSON(df: DataFrame, tableName: String, partitionCount: Int): Unit = {
    exportToS3(df, tableName, partitionCount)
    exportToJSON(df, tableName)
  }

  def exportToS3(df: DataFrame, tableName: String, partitionCount: Int): Unit = {
    val s3StoreUrl = getS3StoreUrl(tableName)
    println(s3StoreUrl)
    df
      .coalesce(partitionCount)
      .write
      .mode("overwrite")
      .option("header", "true")
      .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
      .parquet(s3StoreUrl)
    println("Output to Parquet complete")
  }

  def exportToJSON(df: DataFrame, tableName: String): Unit = {
    val s3StoreUrl = getS3StoreUrl(tableName) + ".json"
    println(s3StoreUrl)
    df
      .coalesce(1)
      .write
      .mode("overwrite")
      .json(s3StoreUrl)
    println("Output to JSON complete")
  }

  def exportToS3(df: DataFrame, tableName: String, partitionColumns: Seq[String], partitionCount: Int): Unit =  {
    val s3StoreUrl = getS3StoreUrl(tableName)
    println(s3StoreUrl)
    df.
      coalesce(partitionCount).
      write.
      partitionBy(partitionColumns: _*).
      mode("overwrite").
      option("header", "true").
      option("codec", "org.apache.hadoop.io.compress.GzipCodec").
      parquet(s3StoreUrl)
    println("Output to Parquet complete")
  }

}
