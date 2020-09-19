package com.github.tharun.datareconciler.transformers.MathUtils

import java.text.DecimalFormat

object CountUtils {

  def percentage(numerator: Long, denominator: Long): Double = {
    round((numerator / denominator.toDouble) * 100)
  }

  def round(number: Double): Double = {
    val formatString = "####.###"
    val formatter = new DecimalFormat(formatString)
    formatter.format(number).toDouble
  }

}
