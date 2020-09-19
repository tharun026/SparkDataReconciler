package com.github.tharun.datareconciler.transformers.dfUtils

import com.github.tharun.datareconciler.framework.TestFramework
import org.junit.runner.RunWith
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner
import org.apache.spark.sql.functions._
import org.apache.spark.sql.Column

@RunWith(classOf[JUnitRunner])
class dfUtilsTest extends TestFramework {

  import spark.implicits._

  val df = Seq((101, "101a", 200.0, 101, "101a", 200.0),
    (101, "101b", 200.0, 101, "101c", 201.0),
    (101, "101c", 200.0, 101, "102c", 200.0))
    .toDF("memb_id", "claim_id", "submitted_amount",
      "new_memb_id", "new_claim_id", "new_submitted_amount")

  "WithColumns -The output of this step " should "match with other dataframe " in {

    val cols: Map[String, Column] = Map("memb_id_match" ->
      when(col("memb_id") <=> col("new_memb_id"), lit(1))
        .otherwise(lit(0)),
      "claim_id_match" ->
        when(col("claim_id") <=> col("new_claim_id"), lit(1))
          .otherwise(lit(0)),
      "submitted_amount_match" ->
        when(col("submitted_amount") <=> col("new_submitted_amount"), lit(1))
          .otherwise(lit(0)))

    val expectedOutput = Seq(
      (101, "101a", 200.0, 101, "101a", 200.0, 1, 1, 1),
      (101, "101b", 200.0, 101, "101c", 201.0, 1, 0, 0),
      (101, "101c", 200.0, 101, "102c", 200.0, 1, 0, 1)
    ).toDF()

    dfUtils.withColumns(df, cols, spark).except(expectedOutput).count() should be (0)

  }

  "selectColumns - The output of this step " should "match with other dataframe " in {

    val columnFunctions: Seq[Column] = Seq(when(col("memb_id") <=> col("new_memb_id"), lit(1))
      .otherwise(lit(0)),
      when(col("claim_id") <=> col("new_claim_id"), lit(1))
        .otherwise(lit(0)),
      when(col("submitted_amount") <=> col("new_submitted_amount"), lit(1))
        .otherwise(lit(0))
    )

    val columnNames: Seq[String] = Seq("memb_id_match", "claim_id_match", "submitted_amount_match")

    println(dfUtils.selectColumns(df, columnNames, columnFunctions, spark).show)
  }

}
