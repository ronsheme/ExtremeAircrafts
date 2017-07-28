package kafka

import java.util.UUID

import akka.{Done, NotUsed}
import position.Position
import rapture.json.Json
import akka.actor.Actor
import akka.event.Logging
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import eventbus.PositionUpdateBus
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import rapture.json.jsonBackends.play._
import rapture.json._

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

class KafkaPositionPublisher extends Actor {

  import KafkaPositionPublisher._

  val producerSettings: ProducerSettings[Array[Byte], String] = ProducerSettings(context.system, new ByteArraySerializer, new StringSerializer).withBootstrapServers(s"$HOST:$PORT")
  val kafkaProducer: KafkaProducer[Array[Byte], String] = producerSettings.createKafkaProducer()
  val log = Logging(context.system, this)
  sys.addShutdownHook(close())
  PositionUpdateBus.subscribePositionUpdate(self)

  override def receive: Receive = {
    case (uuid: UUID, position: position.Position, heading: Double) =>
      log.info(s"Adding to stream new position for aircraft $uuid")
      val toSend = json"""{
                          "uuid": ${uuid.toString},
                          "position": ${Json(position)},
                          "heading": $heading
                      }""".toBareString
      kafkaProducer.send(new ProducerRecord(TOPIC_NAME,toSend))
    case x => log.info(s"recevied unknown message.$x")
  }

  def close(): Unit = {
    //TODO close producer if needed
  }
}

object KafkaPositionPublisher {
  val TOPIC_NAME = "position_updates"
  val HOST = "192.168.174.141"
  //address of vm that hosts kafka.
  val PORT = "9092" //address of vm that hosts kafka

  implicit val positionSerializer: rapture.data.Serializer[Position, Json] =
    Json.serializer[Json].contramap {
      position: Position =>
        json"""{
              "longitude": ${position.longitude},
              "latitude": ${position.latitude},
              "altitude": ${position.altitude}
              }"""
    }
}
