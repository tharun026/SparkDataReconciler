package com.github.tharun.datareconciler.services

import org.apache.spark.sql.SparkSession

class SparkService(local: Boolean, environment: String = "prod") {
  val LOCAL_MASTER = "yarn"
  val APP_NAME = "LDE_Quality -%s"

  val session: SparkSession = createSparkSession

  private def createSparkSession: SparkSession = {
    val builder = SparkSession.builder.appName(APP_NAME.format(environment)).config("spark.driver.maxResultSize", "5g")
    builder.master(LOCAL_MASTER)
    builder.enableHiveSupport()
    val session = builder.getOrCreate()

    session.sparkContext.setLogLevel("ERROR")
    session
  }

  def cleanUpSession: Unit = {
    session.stop()
  }
}
