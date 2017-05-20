package akkaLabs.ExtremeAircrafts.eventbus;

import akka.actor.ActorRef;
import akka.event.japi.LookupEventBus;

public class PositionChangedEventBus extends LookupEventBus<PositionChangedEvelope, ActorRef, String> {

	public static final String POSITION_CHANGED_TOPIC = "PositionChangedTopic";

	// is used for extracting the classifier from the incoming events
	@Override
	public String classify(PositionChangedEvelope event) {
		return event.getDestinationTopic();
	}

	// will be invoked for each event for all subscribers which registered
	// themselves
	// for the event’s classifier
	@Override
	public void publish(PositionChangedEvelope event, ActorRef subscriber) {
		subscriber.tell(event.getPayload(), ActorRef.noSender());
	}

	// must define a full order over the subscribers, expressed as expected from
	// `java.lang.Comparable.compare`
	@Override
	public int compareSubscribers(ActorRef a, ActorRef b) {
		return a.compareTo(b);
	}

	// determines the initial size of the index data structure
	// used internally (i.e. the expected number of different classifiers)
	@Override
	public int mapSize() {
		return 128;
	}

}
