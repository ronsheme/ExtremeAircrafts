package akkaLabs.ExtremeAircrafts.messages.aircraft;

public class MessageEnvelope {

	private String destinationTopic;
	private PositionChangedEvent payload;
	
	public MessageEnvelope(String topic,PositionChangedEvent payload){
		this.destinationTopic = topic;
		this.payload = payload;
	}

	public String getDestinationTopic() {
		return destinationTopic;
	}

	public PositionChangedEvent getPayload() {
		return payload;
	}
}
