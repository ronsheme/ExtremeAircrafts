package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChangeCommand;
import akkaLabs.ExtremeAircrafts.messages.aircraft.AircraftPositionChangeEvent;
import akkaLabs.ExtremeAircrafts.position.Position;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.UUID;

public class Aircraft extends AbstractActor
{
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

	private UUID uuid;
	private Position position;

	private SpatialContext spatialContext;

	public Aircraft(UUID uuid, SpatialContext spatialContext)
	{
		this.uuid = uuid;
		this.spatialContext = spatialContext;
		this.position = new Position();
	}

	private void changePosition(Position newPosition)
	{
		this.position = newPosition;
	}

	@Override
	public Receive createReceive()
	{
		return receiveBuilder().
				match(PositionChangeCommand.class, msg ->
				{
					logger.info("Received position changing message: " + this.position + " -> " + msg.getDestPosition());
					this.changePosition(msg.getDestPosition());
					getContext().actorSelection("../*").tell(new AircraftPositionChangeEvent(this.uuid, msg.getDestPosition()), getSelf());
				}).
				match(AircraftPositionChangeEvent.class, evt ->
				{
					Position position = evt.getPosition();
					Point outerPos = spatialContext.getShapeFactory().multiPoint().pointXYZ(position.getLongitude(), position.getLatitude(), position.getAltitude()).build().getCenter();
					Point innerPos = spatialContext.getShapeFactory().multiPoint().pointXYZ(this.position.getLongitude(), this.position.getLatitude(), this.position.getAltitude()).build().getCenter();
					double distance = spatialContext.calcDistance(innerPos, outerPos); //TODO THIS SHIT ISN'T WORKING AND I'M TIRED!
					logger.info("Brother " + evt.getAircraftId() + " has changed its location to " + position + "... Good for him!");
					if (distance < 0.5)
					{
						logger.info("Wait! Brother is too close! " + distance);
					}
				}).
				build();
	}
}
