package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChangeCommand;
import akkaLabs.ExtremeAircrafts.messages.aircraft.AircraftPositionChangeEvent;
import akkaLabs.ExtremeAircrafts.position.Position;

import java.util.UUID;

public class Aircraft extends AbstractActor
{
	private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);
	private UUID uuid;
	private Position position;

	public Aircraft()
	{//TODO: get this from orchestrator
		this.uuid = UUID.randomUUID();
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
					logger.info("Rx new position: " + msg.getDestPosition());
					this.changePosition(msg.getDestPosition());
					getContext().actorSelection("../*").tell(new AircraftPositionChangeEvent(this.uuid, msg.getDestPosition()), getSelf());
				}).
				match(AircraftPositionChangeEvent.class, evt -> logger.info("brother " + evt.getAircraftId() + " has changed its location to " + evt.getPosition() + " ... Good for him!")).
				build();
	}
}
