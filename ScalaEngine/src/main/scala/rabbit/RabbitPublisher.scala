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
  val HOST = "192.168.198.133"//address of vm that hosts the docker

  //sanity check
  def main(args: Array[String]): Unit = {
    val factory:ConnectionFactory = new ConnectionFactory()
    factory.setHost(HOST)
    val connection:Connection = factory.newConnection()
    val channel:Channel = connection.createChannel()
    channel.queueDeclare(QUEUE_NAME,false,false,false,null)
    val message = "A1"
    channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"))
    channel.close()
    connection.close()
  }
}
