package akkaLabs.ExtremeAircrafts;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import scala.concurrent.duration.Duration;

public class Main {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("Sky");
		ActorRef poller = system.actorOf(Props.create(StateVectorsPoller.class), "poller");
		
		/*
		 * schedule every 5 seconds to send POLL message to the TracksPoller actor
		 */
		system.scheduler().schedule(Duration.Zero(), Duration.create(5000, TimeUnit.MILLISECONDS), ()->{
			poller.tell(StateVectorsPoller.PollerMsg.POLL,ActorRef.noSender());
		}, system.dispatcher());
	}
}
