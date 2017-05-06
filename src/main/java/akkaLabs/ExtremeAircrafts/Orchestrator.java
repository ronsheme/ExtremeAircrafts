package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircraftsCommand;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChangeCommand;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class Orchestrator extends AbstractActor
{
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

	private final Props aircraftProps;
	private final Map<UUID, ActorRef> uuidToActor;

	private final SpatialContext spatialContext;

	private int aircrafts;
	private static final int MIN_SPEED = 10;
	private static final int MAX_SPEED = 200;
	private static final int AUTO_UPDATE = 300;

	private Router router;

	public Orchestrator(SpatialContext spatialContext)
	{
		this.spatialContext = spatialContext;
		this.aircraftProps = Props.create(Aircraft.class, () -> new Aircraft(UUID.randomUUID(), this.spatialContext));
		this.uuidToActor = new HashMap<>();
		this.router = new Router(new RoundRobinRoutingLogic());
	}

	@Override
	public Receive createReceive()
	{
		return receiveBuilder().
				match(ModifyAircraftsCommand.class, msg ->
				{
					int n = msg.getNumOfAircrafts();
					if (this.aircrafts < n)
					{//add aircrafts
						IntStream.range(this.aircrafts, n).forEach(i ->
						{
							UUID uuid = UUID.randomUUID();
							logger.info("Creating actor #" + i + " uuid:" + uuid.toString());
							ActorRef newAircraft = getContext().actorOf(this.aircraftProps, uuid.toString());
							uuidToActor.put(uuid, newAircraft);

							getContext().watch(newAircraft);
							this.router = this.router.addRoutee(newAircraft);
						});
					}
					else if (this.aircrafts > n)
					{    //remove aircrafts
						Iterator<UUID> uuidIterator = uuidToActor.keySet().iterator();
						IntStream.range(n, this.aircrafts).forEach(i ->
						{
							UUID uuid = uuidIterator.next();
							logger.info("Stopping actor with uuid:" + uuid.toString());
							getContext().stop(uuidToActor.get(uuid));
						});
					}
					this.aircrafts = n;
				}).
				match(RequestAircraftsCount.class, msg -> getSender().tell(this.aircrafts, self())).
				match(PositionChangeCommand.class, cmd -> this.router.route(cmd, getSender())).
				match(Terminated.class, terminated -> this.router = router.removeRoutee(terminated.actor())).
				build();
	}

	public int getAircrafts()
	{//for tests
		return this.aircrafts;
	}

	public static class RequestAircraftsCount
	{
	}
}