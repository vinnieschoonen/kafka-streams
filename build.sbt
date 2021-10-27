name := "kafka-streams-training"

version := "0.1"

scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
  "org.slf4j"        % "slf4j-log4j12"         % "1.7.32",
  "org.apache.kafka" % "kafka-clients"         % "2.6.0",
  "org.apache.kafka" % "kafka-streams"         % "2.6.0",
  "io.confluent"     % "kafka-json-serializer" % "6.0.0"
)
