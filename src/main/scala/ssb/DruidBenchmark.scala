package ssb

import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.Warmup
import ssb.infrastructure.DruidClient

object DruidBenchmark {
  private val datasource = sys.env("DRUID_DATASOURCE")

  def query(scenarioName: String): String = {
    val statement = FileUtils.content(s"src/main/resources/druid/$scenarioName.sql")
    DruidClient.query(statement, datasource)
  }
}

@BenchmarkMode(Array(Mode.SingleShotTime))
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@Fork(1)
class DruidBenchmark extends AbstractBenchmark {
  protected def query(scenarioName: String): String = DruidBenchmark.query(scenarioName)
}
