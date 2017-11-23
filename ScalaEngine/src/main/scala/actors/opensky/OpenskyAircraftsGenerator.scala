package actors.opensky

import java.util.UUID

import akka.actor.{Actor, ActorLogging}
import org.opensky.model.StateVector

import scala.collection.mutable

class OpenskyAircraftsGenerator extends Actor with ActorLogging {
  val icao24ToUuid: mutable.Map[String, UUID] = collection.mutable.Map[String, UUID]()

  override def receive: PartialFunction[Any, Unit] ={
    case sv:StateVector =>
      val icao24 = sv.getIcao24
      if (icao24ToUuid.contains(icao24)){
        context.child(icao24ToUuid.get(icao24).toString) match {
          case Some(actorRef) => actorRef ! sv
          case None => log.info(s"icao24 $icao24 actor should exist but is was not found!")
        }
      } else {
        val uuid = UUID.randomUUID()
        icao24ToUuid.put(icao24,uuid)
        context.actorOf(OpenskyAircraft.props(uuid,sv), uuid.toString)
        log.info(s"Created actor with UUID $uuid for state vector with icao24 $icao24")
      }
    case _  => log.info("received unknown message")
  }
}
