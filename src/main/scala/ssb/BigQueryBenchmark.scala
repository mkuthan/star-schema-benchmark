package ssb

import java.util.UUID

import scala.annotation.unused

import org.openjdk.jmh.annotations._
import ssb.infrastructure.BigQueryClient
import ssb.infrastructure.FileUtils

object BigQueryBenchmark {
  private val runId = UUID.randomUUID().toString
  private val project = sys.env("GCP_PROJECT")
  private val dataset = sys.env("GCP_DATASET")

  println(s"BigQueryBenchmark run id: $runId")

  def query(scenarioName: String): String = {
    val statement = FileUtils.content(s"src/main/resources/bigquery/$scenarioName.sql")
    val labels = Map(
      "star-schema-benchmark-run" -> runId,
      "star-schema-benchmark-query" -> scenarioName
    )
    BigQueryClient.query(statement, project, dataset, labels)
  }
}

trait BigQueryBenchmark {
  def query(scenarioName: String): String = BigQueryBenchmark.query(scenarioName)
}

@unused
class BigQuerySerialBenchmark extends SerialBenchmark with BigQueryBenchmark

@unused
class BigQueryConcurrentBenchmark extends ConcurrentBenchmark with BigQueryBenchmark

@BenchmarkMode(Array(Mode.SingleShotTime))
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@Fork(1)
class BigQueryJoinsBenchmark {
  @Benchmark
  def b3_1_joins(): String = BigQueryBenchmark.query("3_1_joins")
}
