package com.vinnieschoonen.kafka.streaming

import org.apache.kafka.streams.StreamsConfig
import java.util.Properties

object Config {
  import org.slf4j.{ Logger, LoggerFactory }

  val logger: Logger = LoggerFactory.getLogger("kafka-streams-training")

  def getSettings: Properties = {
    val settings = new Properties()
    settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-training-1")
    settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")

    settings
  }
}
