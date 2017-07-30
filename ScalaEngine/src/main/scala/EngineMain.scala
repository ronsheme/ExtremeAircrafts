import java.util.concurrent.TimeUnit

import Aircraft.Advance
import Orchestrator.AddAircraft
import akka.actor.{ActorRef, ActorSystem, Props}
import http.PositionUpdater
import kafka.KafkaPositionPublisher
import rabbit.RabbitPositionPublisher

import scala.concurrent.duration.Duration

/**
  * Created by Ron 17/06/2017.
  */
object EngineMain {
  val ORCHESTRATOR_ACTOR = "Orchestrator"
  val ALL_AIRCRAFT_ACTORS: String = "/user/" + ORCHESTRATOR_ACTOR + "/*"
  val UPDATE_RATE_MS = 3000
  val WAIT_AIRCRAFTS_CREATION_MS = 500
  val NUM_OF_AIRCRAFTS = 20

  def main(args: Array[String]): Unit = {
    println("ScalaEngine start..")

    val system = ActorSystem("AircraftsSystem")
    val orchestrator = system.actorOf(Props[Orchestrator], "Orchestrator")
    1 to NUM_OF_AIRCRAFTS foreach {_=>orchestrator ! AddAircraft}

    system.actorOf(Props[PositionUpdater])
    system.actorOf(Props[KafkaPositionPublisher])
    //    system.actorOf(Props[RabbitPositionPublisher])

    system.scheduler.schedule(Duration.create(WAIT_AIRCRAFTS_CREATION_MS,TimeUnit.MILLISECONDS),
      Duration.create(UPDATE_RATE_MS,TimeUnit.MILLISECONDS),
      ()=>system.actorSelection(ALL_AIRCRAFT_ACTORS).tell(Advance,ActorRef.noSender))(system.dispatcher)
  }
}
