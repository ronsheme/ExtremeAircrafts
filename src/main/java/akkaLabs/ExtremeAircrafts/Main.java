package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("Sky");
		ActorRef orchestrator = system.actorOf(Props.create(Orchestrator.class), "orchestrator");
		orchestrator.tell(new Orchestrator.OrchestrationMessage(10),ActorRef.noSender());
	}
}
