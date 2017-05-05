package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("Sky");
		ActorRef creator = system.actorOf(Props.create(AircraftCreator.class), "creator");
		creator.tell(new AircraftCreator.AircraftCreationMessage(1000),ActorRef.noSender());
	}
}
