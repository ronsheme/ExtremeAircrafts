package akkaLabs.ExtremeAircrafts;

import akkaLabs.ExtremeAircrafts.commands.aircraft.AdvanceMessage;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ChangePosition;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvelope;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvent;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEventBus;
import akkaLabs.ExtremeAircrafts.position.Position;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.event.japi.LookupEventBus;
import akka.japi.Pair;

import java.util.UUID;

public class Aircraft extends AbstractActor {
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

	private UUID uuid;
	private double speed;
	private double heading;
	private SpatialContext spatialContext;
	private Pair<Position, Point> location;
	private LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus;
	private long lastUpdateMillis;

	public Aircraft(UUID uuid, double speed, double heading, SpatialContext spatialContext,  LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus) {
		this.heading = heading;
		this.speed = speed;
		this.uuid = uuid;
		this.spatialContext = spatialContext;
		this.eventBus = eventBus;
		this.location = new Pair<>(new Position(), calcPoint(new Position()));
		this.lastUpdateMillis = System.currentTimeMillis();

		eventBus.subscribe(this.getSelf(), PositionChangedEventBus.POSITION_CHANGED_TOPIC);
	}

	private void advance() {
		long currMillis = System.currentTimeMillis();
		long timeDelta = currMillis-this.lastUpdateMillis;
		this.lastUpdateMillis = currMillis;
		double vLat = speed * Math.sin(heading) / 90;
		double vLong = speed * Math.cos(heading) / 180;

		double latDist = timeDelta * vLat;
		double longDist = timeDelta * vLong;

		Position position = location.first();

		this.changePosition(new Position(position.getLongitude() + longDist, position.getLatitude() + latDist, position.getAltitude()));

		logger.info(uuid + " - has advanced to " + this.location.first());

		publishPositionChanged();
	}



	@Override
	public Receive createReceive() {
		return receiveBuilder().
				match(ChangePosition.class, msg ->
				{
					Position newPos = msg.getDestPosition();
					logger.info("Received position changing message: " + this.location.first() + " -> " + newPos);
					this.changePosition(newPos);
					publishPositionChanged();
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
				match(AdvanceMessage.class, msg -> advance()).build();
	}

	public Pair<Position, Point> getLocation() {
		return this.location;
	}

	public double getSpeed(){
		return this.speed;
	}

	public double getHeading(){
		return this.heading;
	}

	private Point calcPoint(Position position) {
		return spatialContext.getShapeFactory().multiPoint().pointXYZ(position.getLongitude(), position.getLatitude(), position.getAltitude()).build().getCenter();
	}

	private void changePosition(Position newPosition)
	{
		this.location = new Pair<>(newPosition, calcPoint(newPosition));
	}

	private void publishPositionChanged(){
		eventBus.publish(new PositionChangedEvelope(new PositionChangedEvent(this.uuid, this.location)));
	}
}
