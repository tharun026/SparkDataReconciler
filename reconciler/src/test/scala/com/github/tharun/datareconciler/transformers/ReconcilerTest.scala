package com.github.tharun.datareconciler.transformers

import com.github.tharun.datareconciler.models._
import com.github.tharun.datareconciler.framework.TestFramework
import org.junit.runner.RunWith
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ReconcilerTest extends TestFramework {

  import spark.implicits._

  "reconcilerDataFramesOptimized - The output of this step " should "match with other dataframe " in {
    val oldTable = Seq((101, "101a", 200.0, 1234567, null, "F", 1), (101, "101b", 200.0, 1236789, null, "M", 0))
      .toDF("memb_id", "claim_id", "submitted_amount", "attending_npi", "rendering_npi", "pt_sex_e", "pt_female_e")
    val newTable = Seq((101, "101a", 200.0, 1234567, null, "F", 1), (101, "101b", 200.0, 123678, null, "M", 0),
      (102, "102a", 201.0, 123678, null, "M", 0))
      .toDF("memb_id", "claim_id", "submitted_amount", "attending_npi", "rendering_npi", "pt_sex_e", "pt_female_e")

    val primaryKey = Seq("memb_id", "claim_id")

    val expectedOutput = Seq(
      ReconcilerModel("submitted_amount", 2, 0, 100.0),
      ReconcilerModel("attending_npi", 1, 1, 50.0),
      ReconcilerModel("rendering_npi", 2, 0, 100.0),
      ReconcilerModel("pt_sex_e", 2, 0, 100.0),
      ReconcilerModel("pt_female_e", 2, 0, 100.0),
      ReconcilerModel("matching_record_count", 2, 0, 100.0),
      ReconcilerModel("dropped_records"),
      ReconcilerModel("new_records", 1, 0, 33.333)
    ).toDS

    Reconciler.reconcileDataFrames(oldTable, newTable, primaryKey, spark)
      .except(expectedOutput).count() should be (0)
  }
}
