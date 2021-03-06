package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.event.japi.LookupEventBus;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ChangePosition;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvelope;
import akkaLabs.ExtremeAircrafts.position.Position;
import com.google.inject.name.Named;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static akkaLabs.ExtremeAircrafts.ExtremeModule.*;

public class Orchestrator extends AbstractActor {

	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
	private final Map<UUID, ActorRef> uuidToActor;
	private final SpatialContext spatialContext;
	private int aircraftsCount;
	@Named(value="positionChangedEventBus")
	private LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus;

	public Orchestrator(SpatialContext spatialContext,LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus) {
		this.spatialContext = spatialContext;
		this.eventBus = eventBus;
		this.uuidToActor = new HashMap<>();
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().
				match(ModifyAircrafts.class, msg -> {
					int n = msg.getNumOfAircrafts();
					if (this.aircraftsCount < n)
					{// add aircrafts
						IntStream.range(this.aircraftsCount, n).forEach(i ->
						{
							UUID uuid = UUID.randomUUID();
							logger.info("Creating actor #" + i + " uuid:" + uuid.toString());
							ActorRef newAircraft = getContext().actorOf(getProps(uuid), uuid.toString());

							double latitude = (MAX_LATITUDE - MIN_LATITUDE) * Math.random() + MIN_LATITUDE;
							double longitude = (MAX_LONGITUDE - MIN_LONGITUDE) * Math.random() + MIN_LONGITUDE;
							newAircraft.tell(new ChangePosition(new Position(longitude, latitude, 15)), ActorRef.noSender());
							uuidToActor.put(uuid, newAircraft);

							getContext().watch(newAircraft);
						});
					}
					else if (this.aircraftsCount > n) { // remove aircrafts
						Iterator<UUID> uuidIterator = uuidToActor.keySet().iterator();
						IntStream.range(n, this.aircraftsCount).forEach(i -> {
							UUID uuid = uuidIterator.next();
							logger.info("Stopping actor with uuid:" + uuid.toString());
							getContext().stop(uuidToActor.get(uuid));
						});
					}
					this.aircraftsCount = n;
				}).
				build();
	}

	public Props getProps(UUID uuid) {
		return Props.create(Aircraft.class, () -> new Aircraft(uuid, MIN_SPEED + Math.random() * MAX_SPEED, Math.random() * 360, this.spatialContext,this.eventBus));
	}

	// for tests
	public int getAircraftsCount()
	{
		return this.aircraftsCount;
	}
}