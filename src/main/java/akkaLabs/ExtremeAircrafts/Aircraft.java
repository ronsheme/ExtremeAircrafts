package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;

public class Aircraft extends AbstractActor
{
	@Override
	public Receive createReceive()
	{
		return receiveBuilder().build();
	}
}
