import java.util.UUID

import akka.actor.{Actor, Props}
import akka.event.Logging
import org.locationtech.spatial4j.context.SpatialContext;

/**
  * Created by Ron on 19/06/2017.
  */
class Aircraft(uuid: UUID, speed: Double, heading: Double, spatialContext: SpatialContext) extends Actor  {
  val log = Logging(context.system, this)

  override def receive = {
    case _  => log.info("Aircraft {}: received unknown message",uuid)
  }
}

object Aircraft {
  /**
    * Create Props for an actor of this type.
    *
    * @param uuid The id to be passed to this actor’s constructor.
    * @return a Props for creating this actor
    */
  def props(uuid: UUID, speed: Double, heading: Double, spatialContext: SpatialContext): Props = Props(new Aircraft(uuid, speed, heading, spatialContext))
}
