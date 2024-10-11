package ssb.infrastructure

import scala.jdk.CollectionConverters._
import scala.util.control.NonFatal

import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.DatasetId
import com.google.cloud.bigquery.QueryJobConfiguration

object BigQueryClient {
  private val queryProject = sys.env("GCP_QUERY_PROJECT")

  private val bqOptions = BigQueryOptions
    .newBuilder()
    .setProjectId(queryProject)
    .build()

  def query(statement: String, project: String, dataset: String, labels: Map[String, String]): String = {
    val queryJobConfiguration = QueryJobConfiguration
      .newBuilder(statement)
      .setDefaultDataset(DatasetId.of(project, dataset))
      .setLabels(labels.asJava)
      .setUseLegacySql(false)
      .setUseQueryCache(false)
      .build()

    try {
      val service = bqOptions.getService
      val response = service.query(queryJobConfiguration)
      val results = response.getValues.toString
      println(results)
      results
    } catch {
      case NonFatal(ex) => throw new RuntimeException(ex)
    }
  }
}
