package ssb

import scala.annotation.unused

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
