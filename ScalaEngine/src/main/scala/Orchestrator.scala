import java.util.UUID

import Orchestrator._
import akka.actor.{Actor, ActorLogging, Props}
import org.locationtech.spatial4j.context.SpatialContextFactory
import position.Position

import scala.util.Random

/**
  * Created by Ron on 19/06/2017.
  */
class Orchestrator extends Actor with ActorLogging {
import Orchestrator.{randomSpeed,randomHeading}

  override def receive ={
    case AddAircraft =>
      val uuid = UUID.randomUUID()
      context.actorOf(Aircraft.props(uuid,randomSpeed,randomHeading,randomPosition,spatialContext), uuid.toString)
      log.info("{}: created aircraft with uuid: {}",self.path.name,uuid)
    case _  => log.info("{}: received unknown message",self.path.name)
  }
}

object Orchestrator{
  case object AddAircraft
  val random = Random
  val MIN_SPEED = 9000;//meter/second
  val MAX_SPEED = 13000;//meter/second
  val MAX_LONGITUDE = 180.0
  val MAX_LATITUDE = 90.0
  val MIN_LONGITUDE = -180.0
  val MIN_LATITUDE = -90.0

  val spatialContext = new SpatialContextFactory().newSpatialContext()

  def randomSpeed = MIN_SPEED + random.nextDouble * MAX_SPEED
  def randomHeading = random.nextDouble * 360
  def randomPosition = new Position((MAX_LATITUDE - MIN_LATITUDE) * random.nextDouble() + MIN_LATITUDE,(MAX_LONGITUDE - MIN_LONGITUDE) * random.nextDouble() + MIN_LONGITUDE)
}
