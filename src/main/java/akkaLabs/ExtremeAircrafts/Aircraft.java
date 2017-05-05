package akkaLabs.ExtremeAircrafts;

import akka.actor.AbstractActor;

public class Aircraft extends AbstractActor {

	@Override
	public Receive createReceive() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public enum AircraftMsg{
		VANISH
	}

}
