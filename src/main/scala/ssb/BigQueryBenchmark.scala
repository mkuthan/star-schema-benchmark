package ssb

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Warmup
import ssb.infrastructure.BigQueryClient

object BigQueryBenchmark {
  private val dataset = sys.env("GCP_DATASET")

  def query(scenarioName: String): String = {
    val statement = FileUtils.content(s"src/main/resources/bigquery/$scenarioName.sql")
    BigQueryClient.query(scenarioName, statement, dataset)
  }
}

@BenchmarkMode(Array(Mode.SingleShotTime))
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@Fork(1)
class BigQueryBenchmark extends AbstractBenchmark {
  protected def query(scenarioName: String): String = BigQueryBenchmark.query(scenarioName)

  @Benchmark
  def b3_1_joins(): String = query("3_1_joins")

}
