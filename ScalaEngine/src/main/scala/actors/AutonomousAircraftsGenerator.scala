package actors

import java.util.UUID

import actors.AutonomousAircraftsGenerator.{AddRandomAircraft, initPosition}
import akka.actor.{Actor, ActorLogging}
import position.Position

import scala.util.Random

/**
  * Created by Ron on 19/06/2017.
  */
class AutonomousAircraftsGenerator extends Actor with ActorLogging {
import AutonomousAircraftsGenerator.{initSpeed, intiHeading}

  override def receive: PartialFunction[Any, Unit] ={
    case AddRandomAircraft =>
      val uuid = UUID.randomUUID()
      context.actorOf(AutonomousAircraft.props(uuid,initSpeed,intiHeading,initPosition), uuid.toString)
      log.info("{}: created random aircraft with uuid: {}",self.path.name,uuid)
    case _  => log.info("{}: received unknown message",self.path.name)
  }
}

object AutonomousAircraftsGenerator{
  case object AddRandomAircraft
  val random = Random
  val MIN_SPEED = 3000;//meter/second
  val MAX_SPEED = 5000;//meter/second
  val MAX_LONGITUDE = 180.0
  val MAX_LATITUDE = 90.0
  val MIN_LONGITUDE: Double = -180.0
  val MIN_LATITUDE: Double = -90.0

  def initSpeed: Double = MIN_SPEED + random.nextDouble * MAX_SPEED
  def intiHeading: Double = random.nextDouble * 360
  def initPosition = new Position((MAX_LATITUDE - MIN_LATITUDE) * random.nextDouble() + MIN_LATITUDE,(MAX_LONGITUDE - MIN_LONGITUDE) * random.nextDouble() + MIN_LONGITUDE)
}
