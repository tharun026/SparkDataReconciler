package com.github.tharun.datareconciler.plumbing

object Benchmark {
  var benchmarkTime: BigDecimal = BigDecimal(0.0)

  def stopwatch(): Double = {
    val currentTime: BigDecimal = BigDecimal(System.currentTimeMillis())
    val elapsed = (currentTime - benchmarkTime) / 1000.0
    benchmarkTime = currentTime

    elapsed.setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

}
