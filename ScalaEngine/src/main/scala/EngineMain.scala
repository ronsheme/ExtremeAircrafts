import java.util.concurrent.TimeUnit

import actors.AutonomousAircraft.Advance
import actors.AutonomousAircraftsGenerator
import actors.kafka.KafkaPositionPublisher
import akka.actor.{ActorRef, ActorSystem, Props}
import actors.http.PositionUpdater
import actors.opensky.OpenskyRequester.RequestFullState
import actors.opensky.{OpenskyAircraftsGenerator, OpenskyRequester}
import actors.rabbit.RabbitPositionPublisher

import scala.concurrent.duration.Duration

/**
  * Created by Ron 17/06/2017.
  */
object EngineMain {
  val ORCHESTRATOR_ACTOR = "actors.Orchestrator"
  val ALL_AIRCRAFT_ACTORS: String = "/user/" + ORCHESTRATOR_ACTOR + "/*"
  val UPDATE_RATE_MS = 10500
  val WAIT_AIRCRAFTS_CREATION_MS = 500

  def main(args: Array[String]): Unit = {
    println("ScalaEngine start..")

    val system = ActorSystem("AircraftsSystem")
    val autonomousAircraftsGenerator = system.actorOf(Props[AutonomousAircraftsGenerator], "actors.autonomousAircraftsGenerator")
    val openskyAircraftsGenerator = system.actorOf(Props[OpenskyAircraftsGenerator], "actors.opensky.aircraftsGenerator")
    val openskyRequester = system.actorOf(Props(new OpenskyRequester(openskyAircraftsGenerator)),"actors.opensky.requester")

    system.actorOf(Props[PositionUpdater])
    //system.actorOf(Props[KafkaPositionPublisher])
    //system.actorOf(Props[RabbitPositionPublisher])

    system.scheduler.schedule(Duration.Zero, Duration.create(UPDATE_RATE_MS,TimeUnit.MILLISECONDS),
            ()=>openskyRequester!RequestFullState)(system.dispatcher)

//    system.scheduler.schedule(Duration.create(WAIT_AIRCRAFTS_CREATION_MS,TimeUnit.MILLISECONDS),
//      Duration.create(UPDATE_RATE_MS,TimeUnit.MILLISECONDS),
//      ()=>system.actorSelection(ALL_AIRCRAFT_ACTORS).tell(Advance,ActorRef.noSender))(system.dispatcher)
  }
}
