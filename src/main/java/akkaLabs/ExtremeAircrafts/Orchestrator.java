package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

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
		return receiveBuilder().match(ModifyAircraftsMsg.class, msg ->
		{
			int n = msg.getNumOfAircrafts();
			if (aircrafts < n)
			{//add aircrafts
				IntStream.range(aircrafts, n).forEach(i ->
				{
					logger.info("Creating actor #" + i);
					aircraftNumToActor.put(i, getContext().actorOf(aircraftProps, String.valueOf(i)));
				});
			}
			else if (aircrafts > n)
			{//remove aircrafts
				IntStream.range(n, aircrafts).forEach(i ->
				{
					logger.info("Stopping actor #" + i);
					getContext().stop(aircraftNumToActor.get(i));
				});
			}
		}).match(RequestAircraftsCount.class, msg ->
		{
			getSender().tell(aircrafts, self());
		}).build();
	}

	public static class ModifyAircraftsMsg
	{
		private int numOfAircrafts;

		public ModifyAircraftsMsg(int numOfAircrafts)
		{
			this.numOfAircrafts = numOfAircrafts;
		}

		public int getNumOfAircrafts()
		{
			return numOfAircrafts;
		}
	}

	public static class RequestAircraftsCount
	{
	}
}