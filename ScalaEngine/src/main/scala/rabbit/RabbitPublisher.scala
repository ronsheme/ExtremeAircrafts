package rabbit

import java.util.UUID

import akka.actor.Actor
import akka.event.Logging
import com.rabbitmq.client.ConnectionFactory

/**
  * Created by ron-lenovo on 7/8/2017.
  */
class RabbitPublisher extends Actor{
  import RabbitPublisher._

  val log = Logging(context.system,this)
  val factory = new ConnectionFactory()
  factory.setHost(HOST)
  val connection = factory.newConnection()
  val channel = connection.createChannel()

  override def receive: Receive = {
    case (uuid: UUID,position: position.Position,heading: Double) => {
      log.info(s"Publishing with new position for aircraft $uuid")
      //TODO continue
    }
    case x => log.info(s"recevied unknown message.$x")
  }
}

object RabbitPublisher{
  val QUEUE_NAME = "hello"
  val HOST = "my-rabbit"
}
