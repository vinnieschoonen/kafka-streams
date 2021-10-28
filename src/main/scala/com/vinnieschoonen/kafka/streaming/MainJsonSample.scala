package com.vinnieschoonen.kafka.streaming

import com.goyeau.kafka.streams.circe.CirceSerdes._
import com.vinnieschoonen.kafka.streaming.Config.{ getSettings, logger }
import io.circe.generic.auto._
import org.apache.kafka.common.serialization.{ Deserializer, Serde, Serdes, Serializer }
import org.apache.kafka.streams.kstream.{ Consumed, Produced }
import org.apache.kafka.streams.{ KafkaStreams, StreamsBuilder }

import java.util.concurrent.CountDownLatch
import scala.collection.JavaConverters._

object MainJsonSample extends App {
  final case class TempReading(station: String, temperature: Double, timestamp: Long)

  def getJsonSerde = {
    val serdeProps = Map[String, Object](
      "json.value.type" -> classOf[TempReading]
    ).asJava

    val temperatureSerializer: Serializer[TempReading]     = implicitly
    val temperatureDeserializer: Deserializer[TempReading] = implicitly

    temperatureSerializer.configure(serdeProps, false)
    temperatureDeserializer.configure(serdeProps, false)

    org.apache.kafka.common.serialization.Serdes.serdeFrom(temperatureSerializer, temperatureDeserializer)
  }

  def getTopology = {
    val builder: StreamsBuilder    = new StreamsBuilder
    val stringSerde: Serde[String] = Serdes.String
    val temperatureSerde           = getJsonSerde

    builder
      .stream("temperatures-topic", Consumed.`with`(stringSerde, temperatureSerde))
      .peek((k, v) => logger.info(s"These are the key: $k and value $v of the stream"))
      .filter((key, value) => value.temperature > 25)
      .to("high-temperatures-topic", Produced.`with`(stringSerde, temperatureSerde))

    builder.build()
  }

  val streams: KafkaStreams = new KafkaStreams(getTopology, getSettings)

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
