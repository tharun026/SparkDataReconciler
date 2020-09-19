package com.github.tharun.datareconciler.transformers.dfUtils

import org.apache.spark.sql.{Column, DataFrame, SparkSession}

object dfUtils {

  /**
    * @param df - the initial dataframe on which the new columns needs to be appended
    * @param cols - a Map of column names and it's definition
    * @param spark
    * @return - dataframe with the new columns
    */

  def withColumns(df: DataFrame, cols: Map[String, Column], spark: SparkSession): DataFrame = {
    cols.keys.foldLeft(df)((df, col) => {
      df.withColumn(col, cols(col))
    })
  }

  def selectColumns(df: DataFrame, columnNames: Seq[String], columnFunctions: Seq[Column], spark: SparkSession): DataFrame = {
    df.select(columnFunctions: _*).toDF(columnNames: _*)
  }

}
