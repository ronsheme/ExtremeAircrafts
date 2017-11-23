package actors.opensky

import java.util

import akka.actor.{Actor, ActorLogging, ActorRef}
import org.opensky.api.OpenSkyApi
import org.opensky.model.StateVector

class OpenskyRequester(aircraftsGeneratorRef:ActorRef) extends Actor with ActorLogging {
  import OpenskyRequester.RequestFullState

  val openskyApi = new OpenSkyApi

  override def receive = {
    case RequestFullState => {
      val opt = Option(openskyApi.getStates(0, null)) match{
        case Some(util.Collection)=> result.forEach(sv=>aircraftsGeneratorRef!sv)
        case None=>log.info("received empty state vector collection")
      }
    }
    case _ => log.info("received unknown message")
  }
}

object OpenskyRequester{
  case object RequestFullState
}