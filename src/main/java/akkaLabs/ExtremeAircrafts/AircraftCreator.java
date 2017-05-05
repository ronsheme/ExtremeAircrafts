package akkaLabs.ExtremeAircrafts;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AircraftCreator extends AbstractActor {

	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
	private Map<Integer,ActorRef> aircraftNumToActor = new HashMap<>();
	private final Props aircraftProps = Props.create(Aircraft.class);
	
	private int aircrafts;

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(AircraftCreationMessage.class, msg -> {
			int n = msg.getNumOfAircrafts();
			if (aircrafts < n) {//add aircrafts
				IntStream.range(aircrafts, n).forEach(i -> {
					logger.info("Creating actor #"+i);
					aircraftNumToActor.put(i, getContext().actorOf(aircraftProps, String.valueOf(i)));
				});
			}
			else if(aircrafts > n){//remove aircrafts
				IntStream.range(n, aircrafts).forEach(i -> {
					logger.info("Sending 'VANISH' message to actor #"+i);
					aircraftNumToActor.get(i).tell(Aircraft.AircraftMsg.VANISH,ActorRef.noSender());
				});
			}
		}).build();
	}

	public static class AircraftCreationMessage {
		private int numOfAircrafts;

		public AircraftCreationMessage(int numOfAircrafts) {
			this.numOfAircrafts = numOfAircrafts;
		}

		public int getNumOfAircrafts() {
			return numOfAircrafts;
		}
	}
}
