package ssb.infrastructure

import scala.util.control.NonFatal

import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.QueryJobConfiguration

object BigQueryClient {
  private val service = BigQueryOptions.getDefaultInstance.getService

  def query(statement: String, dataset: String): String = {
    val queryJobConfiguration = QueryJobConfiguration
      .newBuilder(statement)
      .setDefaultDataset(dataset)
      .setUseLegacySql(false)
      .setUseQueryCache(false)
      .build()

    try {
      val response = service.query(queryJobConfiguration)
      val results = response.getValues.toString
      println(results)
      results
    } catch {
      case NonFatal(ex) => throw new RuntimeException(ex)
    }
  }
}
