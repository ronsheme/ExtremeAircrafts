package akkaLabs.ExtremeAircrafts;

import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class StateVectorsPoller extends AbstractActor {
	private Map<Long,ActorRef> icao24ToActor = new HashMap<>();

	@Override
	public Receive createReceive() {
		return receiveBuilder().matchEquals(PollerMsg.POLL, m->{
			//do polling
			//for each state vector in icao24ToActor keyset send the state vector to the actor
			//for all others create actor and add to icao24ToActor
		}).build();
	}
	
	public static enum PollerMsg{
		POLL
	}

}
