package com.vinnieschoonen.kafka.streaming

import com.vinnieschoonen.kafka.streaming.Config.{ getSettings, logger, sinkTopic, sourceTopic }
import org.apache.kafka.common.serialization.{ Serde, Serdes }
import org.apache.kafka.streams.{ KafkaStreams, StreamsBuilder, Topology }
import org.apache.kafka.streams.kstream.{ Consumed, KStream, Produced }

import java.util.concurrent.CountDownLatch

object MainMapSample extends App {
  logger.info("### Starting Map Sample Application ###")
  val stringSerde: Serde[String] = Serdes.String
  val builder: StreamsBuilder    = new StreamsBuilder
  val sourceTopic                = "lines-topic"
  val sinkTopic                  = "lines-lower-topic"

  val lines: KStream[String, String]       = builder.stream(sourceTopic, Consumed.`with`(stringSerde, stringSerde))
  val transformed: KStream[String, String] = lines.mapValues(value => value.toLowerCase())

  transformed.peek((k, v) => logger.info(s"These are the key: $k and value $v of the stream"))

  transformed.to(sinkTopic, Produced.`with`(stringSerde, stringSerde))

  val topology: Topology    = builder.build
  val streams: KafkaStreams = new KafkaStreams(topology, getSettings)

  val latch: CountDownLatch = new CountDownLatch(1)

  Runtime.getRuntime.addShutdownHook(new Thread(() => {
    logger.info("### Stopping Map Sample Application ###")
    streams.close()
    latch.countDown()
  }))

  try {
    streams.start()
    latch.await()
  } catch {
    case e: Throwable =>
      logger.error(s"Application failed because: $e")
      System.exit(1)
  }
}
