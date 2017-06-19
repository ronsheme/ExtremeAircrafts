import java.util.UUID

import Orchestrator._
import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by Ron on 19/06/2017.
  */
class Orchestrator extends Actor with ActorLogging {

  override def receive ={
    case AddAircraft => {
      val uuid = UUID.randomUUID();
      context.actorOf(Aircraft.props(uuid), uuid.toString)
      log.info("{}: created aircraft with uuid: {}",self.path.name,uuid)
    }
    case _      => log.info("{}: received unknown message",self.path.name)
  }
}

object Orchestrator{
  case object AddAircraft
}
