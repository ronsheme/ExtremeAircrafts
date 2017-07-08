package rabbit

import java.util.UUID

import akka.actor.Actor
import akka.event.Logging
import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}

/**
  * Created by ron-lenovo on 7/8/2017.
  */
class RabbitPublisher extends Actor{
  import RabbitPublisher._

  val log = Logging(context.system,this)

  val factory:ConnectionFactory = new ConnectionFactory()
  factory.setHost(HOST)
  val connection:Connection = factory.newConnection()
  val channel:Channel = connection.createChannel()
  channel.exchangeDeclare(TOPIC_NAME,"fanout")
  sys.addShutdownHook(close())

  override def receive: Receive = {
    case (uuid: UUID,position: position.Position,heading: Double) => {
      log.info(s"Publishing with new position for aircraft $uuid")
      channel.basicPublish(TOPIC_NAME,"",null,position.toString.getBytes())
      //TODO set the message to be a json containing the needed values similarly to PositionChangedHttpEntity
    }
    case x => log.info(s"recevied unknown message.$x")
  }

  def close(): Unit ={
    channel.close()
    connection.close()
  }
}

object RabbitPublisher{
  val TOPIC_NAME = "position_updates"
  val HOST = "192.168.1.107"//address of vm that hosts the docker

  //sanity check
  def main(args: Array[String]): Unit = {

  }
}
