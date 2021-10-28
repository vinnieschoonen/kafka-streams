name := "kafka-streams-training"

version := "0.1"

scalaVersion := "2.12.15"

val circeVersion = "0.14.1"
val kafkaVersion = "2.7.0"
libraryDependencies ++= Seq(
  "org.slf4j"         % "slf4j-log4j12"       % "1.7.32",
  "org.apache.kafka"  % "kafka-clients"       % kafkaVersion,
  "org.apache.kafka"  % "kafka-streams"       % kafkaVersion,
  "com.nequissimus"  %% "circe-kafka"         % kafkaVersion,
  "io.circe"         %% "circe-core"          % circeVersion,
  "io.circe"         %% "circe-generic"       % circeVersion,
  "io.circe"         %% "circe-parser"        % circeVersion,
  "com.goyeau"       %% "kafka-streams-circe" % "fbee94b",
  "org.apache.kafka" %% "kafka-streams-scala" % "2.8.0"
)
