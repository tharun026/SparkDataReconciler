package com.github.tharun.datareconciler.services

import org.slf4j.Logger

object BannerService {
  def printBanner(logger: Logger): Unit = {
    logger.info("""                                                                          """)
    logger.info("""Spark Data Reconciling""")
    logger.info("""                                                                          """)
    logger.info("""""")
  }
}
