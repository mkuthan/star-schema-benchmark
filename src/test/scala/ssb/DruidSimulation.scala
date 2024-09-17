package ssb

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DruidSimulation extends Simulation {
  private val httpProtocol = http
    .baseUrl(sys.env("DRUID_BROKER_URL"))
    .header("Content-Type", "application/json")
    .basicAuth(sys.env("DRUID_USER"), sys.env("DRUID_PASSWORD"))

  private val query = scenario("Druid")
    .foreach(FileUtils.files("src/test/resources/druid"), "data")(
      exec(
        http("#{data(0)}")
          .post("/druid/v2/sql")
          .body(StringBody(
            """
              |{
              |    "query": #{data(1).jsonStringify()},
              |    "useCache": false,
              |    "populateCache": false
              |}
              |""".stripMargin)
          )
      )
    )

  setUp(
    query.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
