enablePlugins(JmhPlugin)

name := "star-schema-benchmark"
scalaVersion := "2.13.15"

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
  // druid client dependencies
  "com.softwaremill.sttp.client3" %% "circe" % "3.9.8",
  "com.softwaremill.sttp.client3" %% "core" % "3.9.8",
  "io.circe" %% "circe-core" % "0.14.9",
  "io.circe" %% "circe-generic" % "0.14.9",
  // bigquery client dependencies
  "com.google.cloud" % "google-cloud-bigquery" % "2.42.4"
)
