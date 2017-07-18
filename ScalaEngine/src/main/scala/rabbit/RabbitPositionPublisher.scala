package rabbit

import java.util.UUID

import akka.actor.Actor
import akka.event.Logging
import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}
import eventbus.PositionUpdateBus
import position.Position
import rapture.json.jsonBackends.play._
import rapture.json._
/**
  * Created by ron-lenovo on 7/8/2017.
  */
class RabbitPositionPublisher extends Actor{
  import RabbitPositionPublisher._

  val log = Logging(context.system,this)

  val factory:ConnectionFactory = new ConnectionFactory()
  factory.setHost(HOST)
  val connection:Connection = factory.newConnection()
  val channel:Channel = connection.createChannel()
  channel.exchangeDeclare(TOPIC_NAME,"fanout")
  sys.addShutdownHook(close())
  PositionUpdateBus.subscribePositionUpdate(self)

  override def receive: Receive = {
    case (uuid: UUID,position: position.Position,heading: Double) => {
      log.info(s"Publishing with new position for aircraft $uuid")

      val eventJson:Json = json"""{
                          "uuid": ${uuid.toString},
                          "position": ${Json(position)},
                          "heading": $heading
                      }"""
      println(eventJson)
      channel.basicPublish(TOPIC_NAME,"",null,eventJson.toString.getBytes())
    }
    case x => log.info(s"recevied unknown message.$x")
  }

  def close(): Unit ={
    channel.close()
    connection.close()
  }
}

object RabbitPositionPublisher{
  val TOPIC_NAME = "position_updates"
  val HOST = "192.168.1.110"//address of vm that hosts the docker

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
