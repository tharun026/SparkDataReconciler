package com.github.tharun.datareconciler

import com.github.tharun.datareconciler.models.CommandConfig
import com.github.tharun.datareconciler.plumbing.CommandFactory
import org.slf4j.{Logger, LoggerFactory}

object Pipeline {
  private val TERMINATION_STRING = "=====================Data Reconciliation Terminated"
  private val STARTUP_STRING = "=====================Data Reconciliation Initialized"

  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit =
  {
    logger.info(STARTUP_STRING)
    CommandConfig.parser.parse(
      args,
      CommandConfig.defaultCommandConfig
    ) match {
      case Some(commandConfig) =>
        CommandFactory.getCommand(commandConfig) match {
          case Some(command) =>
            val statusCode = command.runPipeline

            if (statusCode != 0) {
              logger.info(TERMINATION_STRING.format("ERROR"))
              throw new IllegalStateException(s"Pipeline returned non success code: $statusCode")
            }
          case None =>
            logger.info(TERMINATION_STRING.format("ERROR"))
            throw new IllegalStateException(s"Undefined Job type [jobType]")
        }
      case None =>
        logger.info(TERMINATION_STRING.format("ERROR"))
        throw new IllegalStateException("Bad Argument(s)")
    }
    logger.info(TERMINATION_STRING.format("SUCCESS"))
  }
}
