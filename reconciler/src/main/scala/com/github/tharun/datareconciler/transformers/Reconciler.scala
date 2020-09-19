package com.github.tharun.datareconciler.transformers

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Column
import com.github.tharun.datareconciler.models._
import com.github.tharun.datareconciler.transformers.MathUtils.CountUtils._
import com.github.tharun.datareconciler.transformers.dfUtils.dfUtils._

object Reconciler {

  def reconcileDataFrames(oldTable: DataFrame, newTable: DataFrame, primaryKey: Seq[String],
                          spark: SparkSession):
  Dataset[ReconcilerModel] = {

    import spark.implicits._

    val oldTableCount: Long = oldTable.count
    val newTableCount: Long = newTable.count
    val oldTableColumnsList: Seq[String] = oldTable.columns.toSeq diff primaryKey
    val reconcilerColumnsList: Seq[String] = oldTableColumnsList ++ oldTableColumnsList.map(c => "new_" + c)
    val newTableColumnsList = newTable.columns.toSeq.map(c => {
      if(primaryKey.contains(c)) { c }
      else { "new_" + c}
    })
    val newTableWithColumnsRenamed = newTable.toDF(newTableColumnsList: _*)


    val joinDataFrames = oldTable.join(
      newTableWithColumnsRenamed,
      primaryKey
    )
      .select(reconcilerColumnsList.map(c => col(c)): _*)

    val commonRecordCount = joinDataFrames.count

    val matchColumnsList: Seq[String] = oldTableColumnsList.map(column => column + "_match")
    val matchColumnFunctions: Seq[Column] = oldTableColumnsList.map(column => when(col(column) <=> col("new_" + column), lit(1))
      .otherwise(lit(0)))

    val dataframeWithMatchColumns = selectColumns(joinDataFrames, matchColumnsList, matchColumnFunctions, spark)

    val matchingRecordCounts = dataframeWithMatchColumns.groupBy().sum(matchColumnsList: _*).toDF(oldTableColumnsList: _*)
    matchingRecordCounts.cache

    oldTableColumnsList.map(
      column => {
        val recordsWithSameValues: Long = matchingRecordCounts.select(col(column)).head().getLong(0)
        val recordsWithDifferentValues: Long = commonRecordCount - recordsWithSameValues
        val sameValuesPercentage: Double = percentage(recordsWithSameValues, commonRecordCount)
        Seq(ReconcilerModel(column, recordsWithSameValues, recordsWithDifferentValues, sameValuesPercentage)).toDS
      }
    ).reduce(_ union _)
      .union(
        Seq(
          ReconcilerModel("matching_record_count", commonRecordCount, oldTableCount-commonRecordCount, percentage(commonRecordCount, oldTableCount)),
          ReconcilerModel("dropped_records", oldTableCount-commonRecordCount, 0, percentage(oldTableCount-commonRecordCount, oldTableCount)),
          ReconcilerModel("new_records", newTableCount-commonRecordCount, 0, percentage(newTableCount-commonRecordCount, newTableCount))
        ).toDS)
  }

}
