import Orchestrator.AddAircraft
import akka.actor.{ActorSystem, Props}

/**
  * Created by Ron 17/06/2017.
  */
object EngineMain {
  def main(args: Array[String]): Unit = {
    println("ScalaEngine start..")
    val system = ActorSystem("mySystem")
    val orchestrator = system.actorOf(Props[Orchestrator], "Orchestrator")
    orchestrator ! AddAircraft
  }
}
