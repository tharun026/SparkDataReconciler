# SparkDataReconciler
Spark Data Reconciliation is a tool reconcile two datasets.

# Pre-Requisites
1. Both datasets should have the same set of columns
2. Should have a primary key or a combination of primary keys.

# Usage
/usr/lib/spark/bin/spark-submit --deploy-mode cluster --driver-memory 30G --executor-memory 33G --executor-cores 5 --name Data_Reconciliation --class com.github.tharun.datareconciler.Pipeline {JAR_PATH} --qualityCheckType reconciler --oldTable {PATH_OF_OLD_DATA} --newTable {PATH_OF_NEW_DATA} --outputPath {PATH_OF_OUTPUT_DATA} --primaryKey {COMMA_SEPARATED_PRIMARY_KEYS}

# Sample Output
+--------------------------------------------------------------------------+---------------------+---------------------+--------------------------+
|field_name                                                                |matching_record_count|mismatch_record_count|matching_record_percentage|
|:------------------------------------------------------------------------:|:-------------------:|:-------------------:|:------------------------:|
|student_id                                                                |1000                 |0                    |100.0                     |
|student_first_name                                                        |1000                 |0                    |100.0                     |
|student_middle_name                                                       |1000                 |0                    |100.0                     |
|student_last_name                                                         |1000                 |0                    |100.0                     |
|year                                                                      |1000                 |0                    |100.0                     |
|english                                                                   |1000                 |0                    |100.0                     |
|mathematics                                                               |978                  |22                   |97.8                      |
|physics                                                                   |0                    |1000                 |0.0                       |
|matching_record_count                                                     |1000                 |10                   |99.1                      |
|dropped_records                                                           |10                   |0                    |0.9                       |
|new_records                                                               |30                   |0                    |2.9                       |
|{old_file_path}                                                           |1010                 |0                    |0.0                       |
|{new_file_path}                                                           |1030                 |0                    |0.0                       |
+--------------------------------------------------------------------------+---------------------+---------------------+--------------------------+
