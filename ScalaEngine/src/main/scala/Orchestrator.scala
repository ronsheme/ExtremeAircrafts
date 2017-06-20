import java.util.UUID

import Orchestrator._
import akka.actor.{Actor, ActorLogging, Props}
import org.locationtech.spatial4j.context.SpatialContextFactory

import scala.util.Random

/**
  * Created by Ron on 19/06/2017.
  */
class Orchestrator extends Actor with ActorLogging {
import Orchestrator.{randomSpeed,randomHeading}

  override def receive ={
    case AddAircraft =>
      val uuid = UUID.randomUUID()
      context.actorOf(Aircraft.props(uuid,randomSpeed,randomHeading, spatialContext), uuid.toString)
      log.info("{}: created aircraft with uuid: {}",self.path.name,uuid)
    case _  => log.info("{}: received unknown message",self.path.name)
  }
}

object Orchestrator{
  case object AddAircraft
  val random = Random
  val MIN_SPEED = 10000;//meter/second
  val MAX_SPEED = 16000;//meter/second
  val spatialContext = new SpatialContextFactory().newSpatialContext()

  def randomSpeed = MIN_SPEED + random.nextDouble * MAX_SPEED
  def randomHeading = random.nextDouble * 360
}
