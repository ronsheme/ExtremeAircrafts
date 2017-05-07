package akkaLabs.ExtremeAircrafts;

import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChange;
import akkaLabs.ExtremeAircrafts.position.Position;

public class AircraftTest {
	
	private static ActorSystem sky;
	private static TestActorRef<Orchestrator> orchTestRef;
	private static final String ORCH_NAME = "aircraftTest";
	
	private static TestActorRef<Orchestrator> orchTestActorRef(String name) {
		return TestActorRef.create(sky, Props.create(Orchestrator.class), name);
	}
	
	@BeforeClass
	public static void SetUp()
	{
		Injector injector = Guice.createInjector(new ExtremeModule());
		sky = injector.getInstance(ActorSystem.class);
		orchTestRef = orchTestActorRef(ORCH_NAME);		
		orchTestRef.tell(new ModifyAircrafts(10), ActorRef.noSender());
	}
	
	@Test
	public void positionChangeTest() {
		UUID uuid = UUID.randomUUID();
		TestActorRef<Aircraft> aircraft = TestActorRef.create(sky, orchTestRef.underlyingActor().getProps(uuid), uuid.toString());
		sky.actorSelection("/user/"+uuid.toString()).tell(new PositionChange(new Position(30,30,30)),ActorRef.noSender());
		Assert.assertEquals(aircraft.underlyingActor().getLocation().first().getLatitude(), 30,.0);
	}
	
	@Test
	public void aircraftPositionChangeEventTest(){
		
	}
	
	@Test
	public void advanceTest(){
		
	}
}
