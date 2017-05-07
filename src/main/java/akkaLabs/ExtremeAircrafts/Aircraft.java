package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akkaLabs.ExtremeAircrafts.commands.aircraft.AircraftAdvance;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChange;
import akkaLabs.ExtremeAircrafts.messages.aircraft.AircraftPositionChangeEvent;
import akkaLabs.ExtremeAircrafts.position.Position;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.UUID;

public class Aircraft extends AbstractActor
{
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

	private UUID uuid;
	private double speed;
	private double heading;
	private SpatialContext spatialContext;
	private Pair<Position,Point> location;


	public Aircraft(UUID uuid, double speed, SpatialContext spatialContext) {
		this.speed = speed;
		this.uuid = uuid;
		this.spatialContext = spatialContext;
		this.location = new Pair<Position, Point>(new Position(), calcPoint(new Position()));
	}

	private void advance() {
		//TODO calculate new position using heading and speed and apply changePosition
	}

	private void changePosition(Position newPosition)
	{
		this.location = new Pair<Position, Point>(newPosition, calcPoint(newPosition));;
	}
	
	@Override
	public Receive createReceive()
	{
		return receiveBuilder().
				match(PositionChange.class, msg ->
				{
					Position newPos = msg.getDestPosition();
					logger.info("Received position changing message: " + this.location.first() + " -> " + newPos);
					this.changePosition(newPos);
					getContext().actorSelection("../*").tell(new AircraftPositionChangeEvent(this.uuid, newPos), getSelf());
				}).
				match(AircraftPositionChangeEvent.class, evt ->
				{
					Position position = evt.getPosition();
					Point outerPos = spatialContext.getShapeFactory().multiPoint().pointXYZ(position.getLongitude(), position.getLatitude(), position.getAltitude()).build().getCenter();
					double distance = spatialContext.calcDistance(this.location.second(), outerPos); //TODO THIS SHIT ISN'T WORKING AND I'M TIRED!
					logger.info("Brother " + evt.getAircraftId() + " has changed its location to " + position + "... Good for him!");
					if (distance < 0.5)
					{
						logger.info("Wait! Brother is too close! " + distance);
					}
				})
				.match(AircraftAdvance.class,msg->{
					advance();
				}).
				build();
	}
	
	private Point calcPoint(Position position){
		return spatialContext.getShapeFactory().multiPoint().pointXYZ(position.getLongitude(), position.getLatitude(), position.getAltitude()).build().getCenter();
	}
}
