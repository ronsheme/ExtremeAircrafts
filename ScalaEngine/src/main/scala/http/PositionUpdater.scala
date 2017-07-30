package http

import java.util.UUID

import akka.actor.{Actor, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import com.fasterxml.jackson.databind.ObjectMapper
import eventbus.PositionUpdateBus

/**
  * Created by Ron on 24/06/2017.
  *
  * This actor listens to the position updates event bus and forward the messages to some REST server
  */
class PositionUpdater extends Actor{
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val log = Logging(context.system, this)
  val uri = Uri.from(scheme = "http",host = "localhost",port = 12345, path = "/api/aircrafts/update")
  val mapper = new ObjectMapper()

  PositionUpdateBus.subscribePositionUpdate(self)

  override def receive: Receive = {
    case (uuid: UUID,position: position.Position,heading: Double) => {
      log.info(s"Sending HTTP request with new position for aircraft $uuid")
      Http().singleRequest(HttpRequest(method = HttpMethods.POST,uri = uri, entity = HttpEntity(ContentTypes.`application/json`,
        mapper.writeValueAsString(new PositionChangedHttpEntity(uuid,new Position(position.longitude,position.latitude,position.altitude),heading)))))
      //TODO continue
    }
    case x => log.info(s"recevied unknown message.$x")
  }
}
