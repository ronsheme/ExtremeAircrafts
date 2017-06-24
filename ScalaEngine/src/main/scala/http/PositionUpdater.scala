package http

import java.util.UUID

import akka.actor.Actor
import akka.event.Logging
import position.Position

/**
  * Created by Ron on 24/06/2017.
  *
  * This actor listens to the position updates event bus and forward the messages to some REST server
  */
class PositionUpdater extends Actor{
  val log = Logging(context.system, this)

  override def receive: Receive = {
    case (uuid: UUID,position: Position) => {
      log.debug(s"Sending HTTP request with PositionChangedEvent for aircraft $uuid")
      //TODO continue
    }
    case x:_ => log.info(s"recevied unknown message.$x")
  }
}
