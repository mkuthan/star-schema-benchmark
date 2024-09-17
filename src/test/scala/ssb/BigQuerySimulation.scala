package ssb

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BigQuerySimulation extends Simulation {
  private val gcpProject = sys.env("GCP_PROJECT")
  private val gcpDataset = sys.env("GCP_DATASET")

  private val httpProtocol = http
    .baseUrl("https://bigquery.googleapis.com")
    .header("Content-Type", "application/json")
    .authorizationHeader(s"Bearer ${AuthUtils.getAccessToken}")

  private val query = scenario("BigQuery")
    .foreach(FileUtils.files("src/test/resources/bigquery"), "data")(
      exec(
        http("#{data(0)}")
          .post(s"/bigquery/v2/projects/$gcpProject/queries")
          .body(StringBody(
            s"""
               |{
               |    "query": #{data(1).jsonStringify()},
               |    "defaultDataset": {
               |        "datasetId": "$gcpDataset"
               |    },
               |    "useLegacySql": false,
               |    "useQueryCache": false
               |}
               |""".stripMargin)
          )
          .check(jsonPath("$.jobReference.jobId").saveAs("jobId"))
      ).doWhile(session => session("jobComplete").asOption[String].getOrElse("false") != "true")(
        exec(
          http("Job Status")
            .get(s"/bigquery/v2/projects/$gcpProject/queries/#{jobId}")
            .check(jsonPath("$.jobComplete").saveAs("jobComplete"))
        ).pause(100.millis)
      )
    )

  setUp(
    query.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
