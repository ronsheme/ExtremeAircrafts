package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircraftsCommand;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChangeCommand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import org.locationtech.spatial4j.context.SpatialContext;

import com.google.inject.Guice;

public class Orchestrator extends AbstractActor
{
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
	private Map<UUID, ActorRef> uuidToActor = new HashMap<>();
	private int aircrafts;
	private static final int MIN_SPEED = 10;
	private static final int MAX_SPEED = 200;
	private static final int AUTO_UPDATE = 300;

	@Override
	public Receive createReceive()
	{
		SpatialContext spatialContext = Guice.createInjector(new ExtremeModule()).getInstance(SpatialContext.class);
		return receiveBuilder().
				match(ModifyAircraftsCommand.class, msg ->
				{
					int n = msg.getNumOfAircrafts();
					if (this.aircrafts < n)
					{//add aircrafts
						IntStream.range(this.aircrafts, n).forEach(i ->
						{
							UUID uuid = UUID.randomUUID();
							logger.info("Creating actor #" + i+" uuid:"+uuid.toString());
							uuidToActor.put(uuid, getContext().actorOf(Props.create(Aircraft.class,uuid,MIN_SPEED + Math.random()*MAX_SPEED,spatialContext), uuid.toString()));
						});
					}
					else if (this.aircrafts > n)
					{//remove aircrafts
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
				match(PositionChangeCommand.class, cmd -> getContext().getChildren().iterator().forEachRemaining(ref -> ref.tell(cmd, getSender()))).
				build();
	}

	public int getAircrafts()
	{//for tests
		return this.aircrafts;
	}

	public static class RequestAircraftsCount{
	}
}