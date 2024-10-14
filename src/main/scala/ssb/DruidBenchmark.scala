package ssb

import scala.annotation.unused

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Warmup
import ssb.infrastructure.DruidClient
import ssb.infrastructure.FileUtils

object DruidBenchmark {
  private val datasource = sys.env("DRUID_DATASOURCE")

  def query(scenarioName: String): String = {
    val statement = FileUtils.content(s"src/main/resources/druid/$scenarioName.sql")
    DruidClient.query(statement, datasource)
  }
}

trait DruidBenchmark {
  def query(scenarioName: String): String = DruidBenchmark.query(scenarioName)
}

@unused
class DruidSerialBenchmark extends SerialBenchmark with DruidBenchmark

@unused
class DruidConcurrentBenchmark extends ConcurrentBenchmark with DruidBenchmark

@BenchmarkMode(Array(Mode.SingleShotTime))
@Warmup(iterations = 0)
@Measurement(iterations = 1)
@Fork(1)
class DruidCountDistinctBenchmark {
  @Benchmark
  def b3_1_count_distinct(): String = BigQueryBenchmark.query("3_1_count_distinct")

  @Benchmark
  def b3_1_count_distinct_approx(): String = BigQueryBenchmark.query("3_1_count_distinct_approx")

  @Benchmark
  def b3_4_count_distinct(): String = BigQueryBenchmark.query("3_4_count_distinct")

  @Benchmark
  def b3_4_count_distinct_approx(): String = BigQueryBenchmark.query("3_4_count_distinct_approx")
}
