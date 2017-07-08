package rabbit

import com.rabbitmq.client._

/**
  * Created by ron-lenovo on 7/8/2017.
  */
class RabbitConsumer {

}

object RabbitConsumer{
  val QUEUE_NAME = "hello"
  val HOST = "192.168.1.107"//address of vm that hosts the docker

  //sanity check
  def main(args: Array[String]): Unit = {
    val factory:ConnectionFactory = new ConnectionFactory()
    factory.setHost(HOST)
    val connection:Connection = factory.newConnection()
    val channel:Channel = connection.createChannel()
    channel.queueDeclare(QUEUE_NAME,false,false,false,null)
    val consumer = new DefaultConsumer(channel) {

      override def handleDelivery(consumerTag: String,
                                  envelope: Envelope,
                                  properties: AMQP.BasicProperties,
                                  body: Array[Byte]) {
        var message = new String(body, "UTF-8")
        println(message)
      }
    }
    channel.basicConsume(QUEUE_NAME, true, consumer)
  }
}