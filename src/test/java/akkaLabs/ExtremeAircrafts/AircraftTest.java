package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import scala.concurrent.duration.Duration;

import java.util.UUID;

import static akka.testkit.JavaTestKit.duration;

public class AircraftTest {

	private static ActorSystem system;

	@BeforeClass
	public static void setup() {
		system = ActorSystem.create();
	}

	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system, Duration.create("1 second"),false);
		system = null;
	}

	@Test
	public void testIt() {
		new TestKit(system) {{
			final Props props = Props.create(Orchestrator.class);
			final TestActorRef<Orchestrator> orchestrator = TestActorRef.create(system,props);

			UUID subject1Uuid = UUID.randomUUID();
			UUID subject2Uuid = UUID.randomUUID();
			final ActorRef subject1  = system.actorOf(orchestrator.underlyingActor().getProps(subject1Uuid));
			final ActorRef subject2 = system.actorOf(orchestrator.underlyingActor().getProps(subject2Uuid));
			final TestKit probe = new TestKit(system);
//TODO: implement Event Bus before completing this test. Then make testRef (through TestKit) subscribe to the events
//			subject1.tell(, ActorRef.noSender());
//			subject2.expectMsg(duration("1 second"), "done");
//
//			 the run() method needs to finish within 3 seconds
//			within(duration("3 seconds"), () -> {
//				subject.tell("hello", getRef());
//
//				 This is a demo: would normally use expectMsgEquals().
//				 Wait time is bounded by 3-second deadline above.
//				awaitCond(probe::msgAvailable);
//
//				 response must have been enqueued to us before probe
//				expectMsg(Duration.Zero(), "world");
//				 check that the probe we injected earlier got the msg
//				probe.expectMsg(Duration.Zero(), "hello");
//				Assert.assertEquals(getRef(), probe.getLastSender());
//
//				 Will wait for the rest of the 3 seconds
//				expectNoMsg();
//				return null;
//			});
		}};
	}

//	private static TestActorRef<Orchestrator> orchTestRef;
//	private static final String ORCH_NAME = "aircraftTest";
//	private static Injector injector;


//	@BeforeClass
//	public static void setup()
//	{
//		injector = Guice.createInjector(new ExtremeModule());
//		sky = injector.getInstance(ActorSystem.class);
//		orchTestRef = TestActorRef.create(sky,Props.create(Orchestrator.class,()->new Orchestrator(injector.getInstance(SpatialContext.class))),ORCH_NAME);
//		orchTestRef.tell(new ModifyAircrafts(10), ActorRef.noSender());
//	}

//	@Test
//	public void positionChangeTest() {
//		UUID uuid = UUID.randomUUID();
//		 note- we use the props from the orchestrator but the actor is actually not under the orchestrator, but under /user/
//		TestActorRef<Aircraft> aircraft = TestActorRef.create(sky, orchTestRef.underlyingActor().getProps(uuid), uuid.toString());
//		sky.actorSelection("/user/" + uuid.toString()).tell(new ChangePosition(new Position(30, 30, 30)), ActorRef.noSender());
//		try {
//			Thread.sleep(100);//wait a little for message to be sent
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Assert.assertEquals(30.0, aircraft.underlyingActor().getLocation().first().getLatitude(),0);
//	}
	
//	@Test
//	public void aircraftPositionChangeEventTest(){
//
//	}
	
//	@Test
//	public void advanceTest(){
//
//	}
}
