package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.japi.LookupEventBus;
import akka.testkit.TestActorRef;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import akkaLabs.ExtremeAircrafts.messages.aircraft.MessageEnvelope;

import com.google.inject.Inject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

@Guice(modules = ExtremeModule.class)
public class OrchestratorTest {

	private ActorSystem system = ActorSystem.create("Sky");
	private SpatialContext spatialContext = new SpatialContextFactory().newSpatialContext();
	private LookupEventBus<MessageEnvelope, ActorRef, String> eventBus = new PositionChangedEventBus();
	
	private Map<String,TestActorRef<Orchestrator>> testRefs = new HashMap<>();
	
	@BeforeClass
	public void setup(){
		Arrays.asList("createTest","addTest","removeTest").stream().forEach(name->testRefs.put(name, getTestActorRef(name)));
	}

	@Test
	public void createTest() {
		TestActorRef<Orchestrator> orch = testRefs.get("createTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		Assert.assertEquals(10, orch.underlyingActor().getAircraftsCount());
	}

	@Test
	public void addTest() {
		TestActorRef<Orchestrator> orch =  testRefs.get("addTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		orch.tell(new ModifyAircrafts(15), ActorRef.noSender());
		Assert.assertEquals(15, orch.underlyingActor().getAircraftsCount());
	}

	@Test
	public void removeTest() {
		TestActorRef<Orchestrator> orch =  testRefs.get("removeTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		orch.tell(new ModifyAircrafts(5), ActorRef.noSender());
		Assert.assertEquals(5, orch.underlyingActor().getAircraftsCount());
	}

	private TestActorRef<Orchestrator> getTestActorRef(String name) {
		return TestActorRef.create(system, Props.create(Orchestrator.class, () -> new Orchestrator(spatialContext,eventBus)), name);
	}
}
