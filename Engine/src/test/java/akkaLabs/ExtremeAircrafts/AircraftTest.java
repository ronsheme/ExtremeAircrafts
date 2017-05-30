package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.sysmsg.Terminate;
import akka.event.japi.LookupEventBus;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import akkaLabs.ExtremeAircrafts.commands.aircraft.AdvanceMessage;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvelope;

import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvent;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEventBus;
import akkaLabs.ExtremeAircrafts.position.Position;
import org.locationtech.spatial4j.context.SpatialContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import scala.concurrent.duration.Duration;

import java.util.UUID;

import static akkaLabs.ExtremeAircrafts.ExtremeModule.UPDATE_RATE;

@Guice(moduleFactory = ExtremeModuleFactory.class)
public class AircraftTest {

	@Inject
	private ActorSystem system;
	@Inject
	private SpatialContext spatialContext;
	@Inject
	private LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus;

	private TestActorRef<Orchestrator> orchestrator;

	@BeforeClass
	public void setup(){
		this.orchestrator = TestActorRef.create(this.system, Props.create(Orchestrator.class, () -> new Orchestrator(this.spatialContext,this.eventBus)), "orch");
	}


	@AfterClass
	public void teardown() {
		this.orchestrator.sendSystemMessage(new Terminate());
		TestKit.shutdownActorSystem(this.system, Duration.create("1 second"),false);
		system = null;
	}

	@Test
	public void testPositionChangedPublish() {
		new TestKit(this.system) {{
			UUID subject1Uuid = UUID.randomUUID();
			ActorRef subject1  = system.actorOf(orchestrator.underlyingActor().getProps(subject1Uuid));
			eventBus.subscribe(getRef(),PositionChangedEventBus.POSITION_CHANGED_TOPIC);

			subject1.tell(new AdvanceMessage(), ActorRef.noSender());
			expectMsgClass(duration("1 second"), PositionChangedEvent.class);
		}};
	}

	@Test
	public void advanceTest(){
		UUID uuid = UUID.randomUUID();
		TestActorRef<Aircraft> aircraftActor = TestActorRef.create(this.system,orchestrator.underlyingActor().getProps(uuid));

		Aircraft underlying = aircraftActor.underlyingActor();
		double latDist = UPDATE_RATE * underlying.getSpeed() * Math.sin(underlying.getHeading()) / 90;
		double longDist = UPDATE_RATE * underlying.getSpeed() * Math.cos(underlying.getHeading()) / 180;
		Position expectedPosition = new Position(underlying.getLocation().first().getLongitude() + longDist, underlying.getLocation().first().getLatitude() + latDist, underlying.getLocation().first().getAltitude());

		TestKit listener = new TestKit(this.system);
		this.eventBus.subscribe(listener.getRef(),PositionChangedEventBus.POSITION_CHANGED_TOPIC);

		aircraftActor.tell(new AdvanceMessage(),ActorRef.noSender());
		listener.expectMsgPF("",event->{
			PositionChangedEvent posEvent  = PositionChangedEvent.class.cast(event);
			Assert.assertEquals(expectedPosition.getAltitude(),posEvent.getPosition().getAltitude());
			Assert.assertEquals(expectedPosition.getLatitude(),posEvent.getPosition().getLatitude());
			Assert.assertEquals(expectedPosition.getLongitude(),posEvent.getPosition().getLongitude());
			return true;
		});
	}
}
