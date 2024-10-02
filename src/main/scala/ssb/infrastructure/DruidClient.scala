package ssb.infrastructure


import io.circe.generic.auto._
import sttp.client3._
import sttp.client3.circe._

object DruidClient {

  private case class DruidRequest(
      query: String,
      useCache: Boolean,
      populateCache: Boolean,
      useResultLevelCache: Boolean,
      populateResultLevelCache: Boolean
  )

  private val backend = HttpClientSyncBackend()

  private val broker = sys.env("DRUID_BROKER_URL")
  private val user = sys.env("DRUID_USER")
  private val password = sys.env("DRUID_PASSWORD")

  def query(statement: String, datasource: String): String = {
    val druidRequest = DruidRequest(
      query = statement.replace("__DRUID_DATASOURCE__", datasource),
      useCache = false,
      populateCache = false,
      useResultLevelCache = false,
      populateResultLevelCache = false
    )

    val httpRequest = basicRequest
      .post(uri"$broker/druid/v2/sql")
      .contentType("application/json")
      .auth.basic(user, password)
      .body(druidRequest)
      .response(asString)

    val httpResponse = backend.send(httpRequest)
    httpResponse.body match {
      case Left(value) => throw new RuntimeException(value)
      case Right(value) => println(value); value
    }
  }
}
