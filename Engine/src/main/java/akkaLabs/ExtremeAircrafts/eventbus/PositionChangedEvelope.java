package akkaLabs.ExtremeAircrafts.eventbus;

public class PositionChangedEvelope {

	private String destinationTopic;
	private PositionChangedEvent payload;
	
	public PositionChangedEvelope(PositionChangedEvent payload){
		this.destinationTopic = PositionChangedEventBus.POSITION_CHANGED_TOPIC;
		this.payload = payload;
	}

	public String getDestinationTopic() {
		return destinationTopic;
	}

	public PositionChangedEvent getPayload() {
		return payload;
	}
}
