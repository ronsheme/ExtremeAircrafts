package actors.kafka

import java.util.{Properties, UUID}

import akka.actor.Actor
import akka.event.Logging
import eventbus.PositionUpdateBus
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import position.Position
import rapture.json.{Json, _}
import rapture.json.jsonBackends.play._

class KafkaPositionPublisher extends Actor {

  import KafkaPositionPublisher._

  val kafkaProducer = new KafkaProducer[String, String](props)
  val log = Logging(context.system, this)
  sys.addShutdownHook(close())
  PositionUpdateBus.subscribePositionUpdate(self)

  override def receive: Receive = {
    case (uuid: UUID, position: position.Position, heading: Double) =>
      val toSend =
        json"""{
                          "position": ${Json(position)},
                          "heading": $heading
                      }""".toBareString
      log.info(s"Producing to kafka $toSend")
      kafkaProducer.send(new ProducerRecord(TOPIC_NAME, s"$uuid", toSend))
    case x => log.info(s"recevied unknown message.$x")
  }

  def close(): Unit = {
    kafkaProducer.close()
  }
}

object KafkaPositionPublisher {
  val TOPIC_NAME = "position_updates"
  val HOST = "localhost"
  //address of vm that hosts kafka.
  val PORT = "9092" //address of vm that hosts kafka
  val props = new Properties()
  props.put("bootstrap.servers", s"$HOST:$PORT")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

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
