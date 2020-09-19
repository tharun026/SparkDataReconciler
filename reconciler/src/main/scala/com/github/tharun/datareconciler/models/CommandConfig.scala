package com.github.tharun.datareconciler.models

import scopt.OptionParser

case object CommandConfig {
  private val OPTION_QUALITY_CHECK_TYPE = "qualityCheckType"
  private val OPTION_OLD_TABLE = "oldTable"
  private val OPTION_NEW_TABLE = "newTable"
  private val OPTION_PRIMARY_KEY = "primaryKey"
  private val OPTION_OUTPUT_PATH = "outputPath"

  val defaultCommandConfig = CommandConfig(
    qualityCheckType = "reconciler",
    oldTable = "",
    newTable = "",
    primaryKey = "",
    outputPath = ""
  )

  val parser: OptionParser[CommandConfig] = new OptionParser[CommandConfig]("scopt") {
    opt[String](OPTION_QUALITY_CHECK_TYPE)
      .action((qualityCheckType, commandConfig) => commandConfig.copy(qualityCheckType = qualityCheckType))
      .text("type of QC that needs to be performed")

    opt[String](OPTION_OLD_TABLE)
      .action((oldTable, commandConfig) => commandConfig.copy(oldTable = oldTable))
      .text("Path of the oldTable")

    opt[String](OPTION_NEW_TABLE)
      .action((newTable, commandConfig) => commandConfig.copy(newTable = newTable))
      .text("Path of the newTable")

    opt[String](OPTION_PRIMARY_KEY)
      .action((primaryKey, commandConfig) => commandConfig.copy(primaryKey = primaryKey))
      .text("Comma separated list of primary keys")

    opt[String](OPTION_OUTPUT_PATH)
      .action((outputPath, commandConfig) => commandConfig.copy(outputPath = outputPath))
      .text("outputPath where QC results are to be stored")
  }
}

case class CommandConfig (
                           qualityCheckType: String,
                           oldTable: String,
                           newTable: String,
                           primaryKey: String,
                           outputPath: String
                         )
