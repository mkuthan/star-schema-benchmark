package ssb

import org.openjdk.jmh.annotations.Benchmark

abstract class AbstractBenchmark {
  @Benchmark
  def b1_1(): String = query("1_1")

  @Benchmark
  def b1_2(): String = query("1_2")

  @Benchmark
  def b1_3(): String = query("1_3")

  @Benchmark
  def b2_1(): String = query("2_1")

  @Benchmark
  def b2_2(): String = query("2_2")

  @Benchmark
  def b2_3(): String = query("2_3")

  @Benchmark
  def b3_1(): String = query("3_1")

  @Benchmark
  def b3_2(): String = query("3_2")

  @Benchmark
  def b3_3(): String = query("3_3")

  @Benchmark
  def b3_4(): String = query("3_4")

  @Benchmark
  def b4_1(): String = query("4_1")

  @Benchmark
  def b4_2(): String = query("4_2")

  @Benchmark
  def b4_3(): String = query("4_3")

  protected def query(scenarioName: String): String
}
