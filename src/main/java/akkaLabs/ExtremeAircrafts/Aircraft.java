package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChangeCommand;
import akkaLabs.ExtremeAircrafts.messages.aircraft.AircraftPositionChangeEvent;
import akkaLabs.ExtremeAircrafts.position.Position;

import java.util.UUID;

import org.locationtech.spatial4j.context.SpatialContext;

import com.google.inject.Guice;

public class Aircraft extends AbstractActor {
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
	private UUID uuid;
	private Position position;
	private double speed;
	private double heading;
	private SpatialContext spatialContext;

	public Aircraft(UUID uuid, double speed, SpatialContext spatialContext) {
		this.speed = speed;
		this.uuid = uuid;
		this.spatialContext = spatialContext;
	}

	private void advance() {
		//TODO calculate new position using heading and speed and apply changePosition
	}

	private void changePosition(Position newPosition) {
		this.position = newPosition;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(PositionChangeCommand.class, msg -> {
			logger.info("Rx new position: " + msg.getDestPosition());
			// System.out.println("Rx new position: " + msg.getDestPosition());
			this.changePosition(msg.getDestPosition());//TODO call advance instead
			getContext().actorSelection("../*").tell(new AircraftPositionChangeEvent(this.uuid, msg.getDestPosition()),
					getSelf());
		}).match(AircraftPositionChangeEvent.class, evt -> logger.info("brother " + evt.getAircraftId()
				+ " has changed its location to " + evt.getPosition() + " ... Good for him!")).build();
	}
}
