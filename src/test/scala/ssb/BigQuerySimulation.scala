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

  private val regularQuery = scenario("BigQuery Regular Queries")
    .foreach(FileUtils.files("src/test/resources/bigquery"), "data")(
      group("#{data(0)}")(
        exec(
          http("Regular Query")
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
            .check(jmesPath("jobReference.jobId").saveAs("regularJobId")),
        ).group("Await Job Results")(
          doWhile(session => session("jobComplete").asOption[String].getOrElse("false") != "true")(
            exec(
              http("Check Job Status")
                .get(s"/bigquery/v2/projects/$gcpProject/queries/#{regularJobId}")
                .check(jmesPath("jobComplete").saveAs("jobComplete"))
            ).pause(100.millis)
          )
        )
      )
    )

  private val shortQuery = scenario("BigQuery Short Queries")
    .foreach(FileUtils.files("src/test/resources/bigquery"), "data")(
      group("#{data(0)}")(
        exec(
          http("Short Query")
            .post(s"/bigquery/v2/projects/$gcpProject/queries")
            .body(StringBody(
              s"""
                 |{
                 |    "query": #{data(1).jsonStringify()},
                 |    "defaultDataset": {
                 |        "datasetId": "$gcpDataset"
                 |    },
                 |    "useLegacySql": false,
                 |    "useQueryCache": false,
                 |    "jobCreationMode": "JOB_CREATION_OPTIONAL"
                 |}
                 |""".stripMargin)
            )
            .check(jmesPath("jobReference.jobId").optional.saveAs("shortJobId"))
        ).group("Await Job Results")(
          doIf("#{shortJobId.exists()}") {
            doWhile(session => session("jobComplete").asOption[String].getOrElse("false") != "true")(
              exec(
                http("Check Job Status")
                  .get(s"/bigquery/v2/projects/$gcpProject/queries/#{regularJobId}")
                  .check(jmesPath("jobComplete").saveAs("jobComplete"))
              ).pause(100.millis)
            )
          }
        )
      )
    )

  setUp(
    regularQuery.inject(atOnceUsers(1)).andThen(shortQuery.inject(atOnceUsers(1)))
  ).protocols(httpProtocol)
}
