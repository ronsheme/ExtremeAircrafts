package actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.Logging
import eventbus.{PositionEnvelope, PositionUpdateBus}
import position.{Position, PositionUtil}

import scala.util.Random

/**
  * Created by Ron on 19/06/2017.
  */
class AutonomousAircraft(uuid: UUID, var speed: Double, var heading: Double, var position: Position) extends Actor with ActorLogging{
  import AutonomousAircraft._

  PositionUpdateBus.subscribePositionUpdate(self)
  var lastUpdateMillis: Long = System currentTimeMillis

  def advance(){
    val currMillis = System currentTimeMillis
    val timeDeltaSeconds = (currMillis - this.lastUpdateMillis) / 1000.0
    lastUpdateMillis = currMillis
    heading += randomHeading
    heading %= 360
    position = PositionUtil.calculate(timeDeltaSeconds * speed, heading, position)
    publishPositionUpdate(uuid,position,heading)
    log.debug( "{} - has advanced to {}",uuid, position)
}

  override def receive: PartialFunction[Any, Unit] = {
    case Advance => advance()
    case (uuid: UUID,position: Position,heading:Double) => log.debug(s"Received message from event bus. $uuid moved to $position")
    case _  => log.info("received unknown message")
  }
}

object AutonomousAircraft {
  case object Advance
  val random  = Random

  /**
    * Create Props for an actor of this type.
    *
    * @param uuid The id to be passed to this actor’s constructor.
    * @return a Props for creating this actor
    */
  def props(uuid: UUID, speed: Double, heading: Double,position: Position): Props = Props(new AutonomousAircraft(uuid, speed, heading, position))

  def randomHeading():Double = {
    val randomDouble = random.nextDouble
    if(randomDouble > 0.5)
      randomDouble * 10
    else
      -randomDouble * 10
  }

  def publishPositionUpdate(uuid: UUID, position: Position,heading: Double): Unit = PositionUpdateBus.publish(PositionEnvelope(PositionUpdateBus.POSITION_UPDATE_TOPIC,(uuid,position,heading)))
}
