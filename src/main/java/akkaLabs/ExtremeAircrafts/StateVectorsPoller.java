package akkaLabs.ExtremeAircrafts;

import java.util.HashMap;
import java.util.Map;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class StateVectorsPoller extends AbstractActor {
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
	private Map<Long,ActorRef> icao24ToActor = new HashMap<>();

	@Override
	public Receive createReceive() {
		return receiveBuilder().matchEquals(PollerMsg.POLL, m->{
			logger.info("Start polling.");
			//do polling
			logger.info("Start state vector actors update.");	
			//for each state vector in icao24ToActor keyset send the state vector to the actor
			logger.info("Create new state vector actors.");
			//for all others create actor and add to icao24ToActor		
		}).build();
	}
	
	public static enum PollerMsg{
		POLL
	}

}
