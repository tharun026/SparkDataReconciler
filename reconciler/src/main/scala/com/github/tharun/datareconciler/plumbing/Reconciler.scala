package com.github.tharun.datareconciler.plumbing

import com.github.tharun.datareconciler.models.{CommandConfig, ReconcilerModel}
import com.github.tharun.datareconciler.transformers.Reconciler._

class Reconciler(commandConfig: CommandConfig) extends Command(commandConfig: CommandConfig) {

  override def execute(): Unit = {

    import spark.implicits._

    val oldTableDF = spark.read.parquet(commandConfig.oldTable)
    val newTableDF = spark.read.parquet(commandConfig.newTable)
    val primaryKey = commandConfig.primaryKey.split(",").toSeq

    val reconcileDataFrameStats = reconcileDataFrames(oldTableDF, newTableDF, primaryKey, spark)
    val oldTableStats = Seq(ReconcilerModel(commandConfig.oldTable, oldTableDF.count)).toDS
    val newTableStats = Seq(ReconcilerModel(commandConfig.newTable, newTableDF.count)).toDS

    val reconciliationResults = reconcileDataFrameStats.union(oldTableStats).union(newTableStats)

    dataExportService.exportToS3(
      reconciliationResults.toDF,
      commandConfig.oldTable.split("/").last + "_reconciled",
      1
    )
  }
}
