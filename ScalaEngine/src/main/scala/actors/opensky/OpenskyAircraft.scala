package actors.opensky

import java.util.UUID

import actors.AutonomousAircraft
import actors.AutonomousAircraft.publishPositionUpdate
import akka.actor.{Actor, ActorLogging, Props}
import org.opensky.model.StateVector
import position.Position

class OpenskyAircraft (uuid: UUID, var speed: Double, var heading: Double, var position: Position) extends Actor with ActorLogging {
  import OpenskyAircraft.calcSpeed

  override def receive: PartialFunction[Any, Unit] = {
    case state:StateVector =>
      log.info(s"received state vector update")
      val newPos = new Position(state getLongitude,state getLatitude)
      speed = calcSpeed(position, newPos)
      heading = state getHeading()
      position = newPos
      publishPositionUpdate(uuid,position,heading)
    case _  => log.info("received unknown message")
  }
}

object OpenskyAircraft {
  def calcSpeed(a:Position,b:Position):Double = Double.NaN //TODO: actually calculate speed
  def props(uuid: UUID, stateVector: StateVector): Props = Props(new AutonomousAircraft(uuid, Double.NaN, stateVector getHeading, new Position(stateVector getLongitude, stateVector getLatitude)))
}
