package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircraftsCommand;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChangeCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Orchestrator extends AbstractActor
{
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
	private final Props aircraftProps = Props.create(Aircraft.class);
	private Map<Integer, ActorRef> aircraftNumToActor = new HashMap<>();
	private int aircrafts;

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
							logger.info("Creating actor #" + i);
							aircraftNumToActor.put(i, getContext().actorOf(aircraftProps, String.valueOf(i)));
						});
					}
					else if (this.aircrafts > n)
					{//remove aircrafts
						IntStream.range(n, this.aircrafts).forEach(i ->
						{
							logger.info("Stopping actor #" + i);
							getContext().stop(aircraftNumToActor.get(i));
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