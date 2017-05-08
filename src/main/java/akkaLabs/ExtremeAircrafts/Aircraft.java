package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Pair;
import akkaLabs.ExtremeAircrafts.commands.aircraft.AdvanceAircraft;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ChangePosition;
import akkaLabs.ExtremeAircrafts.messages.aircraft.PositionChangedEvent;
import akkaLabs.ExtremeAircrafts.position.Position;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.UUID;

import static akkaLabs.ExtremeAircrafts.ExtremeModule.RATE;

public class Aircraft extends AbstractActor
{
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

	private UUID uuid;
	private double speed;
	private double heading;
	private SpatialContext spatialContext;
	private Pair<Position, Point> location;

	public Aircraft(UUID uuid, double speed, double heading, SpatialContext spatialContext)
	{
		this.heading = heading;
		this.speed = speed;
		this.uuid = uuid;
		this.spatialContext = spatialContext;
		this.location = new Pair<>(new Position(), calcPoint(new Position()));
	}

	private void advance()
	{
		double vLat = speed * Math.sin(heading) / 90;
		double vLong = speed * Math.cos(heading) / 180;

		double latDist = RATE * vLat;
		double longDist = RATE * vLong;

		Position position = location.first();

		this.changePosition(new Position(position.getLongitude() + longDist, position.getLatitude() + latDist, position.getAltitude()));

		logger.info(uuid + " - has advanced to " + this.location.first());
		// TODO calculate new position using heading and speed and apply
		// changePosition
	}

	private void changePosition(Position newPosition)
	{
		this.location = new Pair<>(newPosition, calcPoint(newPosition));
	}

	@Override
	public Receive createReceive()
	{
		return receiveBuilder().
				match(ChangePosition.class, msg ->
				{
					Position newPos = msg.getDestPosition();
					logger.info("Received position changing message: " + this.location.first() + " -> " + newPos);
					this.changePosition(newPos);
					getContext().actorSelection("../*").tell(new PositionChangedEvent(this.uuid, this.location), getSelf());
				}).
				match(PositionChangedEvent.class, evt ->
				{
					if (!evt.getAircraftId().equals(this.uuid))
					{
						Point outerPos = evt.getPoint();
						double distance = spatialContext.calcDistance(this.location.second(), outerPos); // TODO THIS SHIT ISN'T WORKING AND I'M TIRED!
						logger.info("Brother " + evt.getAircraftId() + " had changed its location to " + evt.getPosition() + "... Good for him!");
						if (distance < 0.03)
						{
							logger.info("Wait! Brother " + evt.getAircraftId() + " is too close! " + distance);
						}
					}
				}).
				match(AdvanceAircraft.class, msg -> advance()).build();
	}

	private Point calcPoint(Position position)
	{
		return spatialContext.getShapeFactory().multiPoint().pointXYZ(position.getLongitude(), position.getLatitude(), position.getAltitude()).build().getCenter();
	}

	public Pair<Position, Point> getLocation()
	{
		return this.location;
	}
}
