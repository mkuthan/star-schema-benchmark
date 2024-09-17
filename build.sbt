enablePlugins(GatlingPlugin)

name := "star-schema-benchmark"
scalaVersion := "2.13.10"

scalacOptions := Seq(
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Wdead-code",
  "-Wnumeric-widen",
  "-Wunused"
)

libraryDependencies ++= Seq(
  "io.gatling" % "gatling-test-framework" % "3.11.5" % Test,
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.11.5" % Test,
  "com.google.auth" % "google-auth-library-oauth2-http" % "1.23.0" % Test
)
