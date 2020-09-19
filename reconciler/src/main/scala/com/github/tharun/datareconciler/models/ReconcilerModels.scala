package com.github.tharun.datareconciler.models

case class ReconcilerModel(field_name: String = null, matching_record_count: Long = 0, mismatch_record_count: Long = 0,
                           matching_record_percentage: Double = 0.0)

case class PartitionedReconcilerModel(old_table: String, new_table: String, partition_value: String = null,
                                      field_name: String = null, matching_record_count: Long = 0,
                                      mismatch_record_count: Long = 0, matching_record_percentage: Double = 0.0)
