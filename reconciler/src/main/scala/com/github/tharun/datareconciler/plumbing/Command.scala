package com.github.tharun.datareconciler.plumbing

import com.github.tharun.datareconciler.models.CommandConfig
import com.github.tharun.datareconciler.services.{BannerService, DataExportService, SparkService}
import org.apache.spark.sql.SparkSession
import org.slf4j.{Logger, LoggerFactory}

abstract class Command(commandConfig: CommandConfig) {
  protected val STATUS_CODE_SUCCESS = 0
  protected val STATUS_CODE_FAIL = 2
  @transient lazy protected val logger: Logger = LoggerFactory.getLogger(this.getClass)
  @transient lazy protected val sparkService = new SparkService(local= false)
  @transient lazy protected val spark: SparkSession = sparkService.session
  @transient lazy protected val dataExportService = new DataExportService(commandConfig.outputPath, spark)

  def runPipeline: Int = {
    try {
      Benchmark.stopwatch()
      BannerService.printBanner(logger)

      logger.info(s" ++ Quality Check Type: ${commandConfig.qualityCheckType}")
      logger.info(s" ++ Output Path: ${commandConfig.outputPath}")
      logger.info(s" ++ Old Table: ${commandConfig.oldTable}")
      logger.info(s" ++ New Table: ${commandConfig.newTable}")
      logger.info(s" ++ Primary Key: ${commandConfig.primaryKey}")

      execute()
      sparkService.cleanUpSession
      logger.info(s"  ++ Processing completed in ${Benchmark.stopwatch()} seconds")
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
        logger.error(e.getMessage + "\n" + e.getStackTrace)
        return STATUS_CODE_FAIL
      }
    }
    STATUS_CODE_SUCCESS
  }

  def execute(): Unit
}