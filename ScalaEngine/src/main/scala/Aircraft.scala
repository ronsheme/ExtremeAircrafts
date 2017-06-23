import java.util.UUID

import akka.actor.{Actor, Props}
import akka.event.Logging
import org.locationtech.spatial4j.context.SpatialContext
import position.{Position, PositionUtil}

import scala.util.Random;

/**
  * Created by Ron on 19/06/2017.
  */
class Aircraft(uuid: UUID,var speed: Double, var heading: Double,var position: Position, spatialContext: SpatialContext) extends Actor {
  import Aircraft.randomHeading

  val log = Logging(context.system, this)
  var lastUpdateMillis = System.currentTimeMillis()


  def advance(){
    val currMillis = System.currentTimeMillis()
    val timeDeltaSeconds = (currMillis - this.lastUpdateMillis) / 1000.0
    lastUpdateMillis = currMillis;
    heading += randomHeading
    position = PositionUtil.calculate(timeDeltaSeconds * speed, heading, position)
    //TODO continue here
//    publishPositionChanged();
    log.info( "{} - has advanced to {}",uuid, position)
}

  override def receive = {
    case _  => log.info("Aircraft {}: received unknown message",uuid)
  }

  def changePosition = {

  }
}

object Aircraft {
  val random  = Random

  /**
    * Create Props for an actor of this type.
    *
    * @param uuid The id to be passed to this actor’s constructor.
    * @return a Props for creating this actor
    */
  def props(uuid: UUID, speed: Double, heading: Double,position: Position, spatialContext: SpatialContext): Props = Props(new Aircraft(uuid, speed, heading, position, spatialContext))

  def randomHeading():Double = {
    val randomDouble = random.nextDouble
    if(randomDouble > 0.5)
      randomDouble * 10
    else
      -randomDouble * 10
  }

}
