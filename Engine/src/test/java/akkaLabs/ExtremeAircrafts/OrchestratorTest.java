package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.japi.LookupEventBus;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvelope;

import com.google.inject.Inject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.locationtech.spatial4j.context.SpatialContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import scala.concurrent.duration.Duration;

@Guice(moduleFactory = ExtremeModuleFactory.class)
public class OrchestratorTest {

	@Inject
	private ActorSystem system;
	@Inject
	private SpatialContext spatialContext;
	@Inject
	private LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus;
	
	private Map<String,TestActorRef<Orchestrator>> testRefs = new HashMap<>();
	
	@BeforeClass
	public void setup(){
		Arrays.asList("createTest","addTest","removeTest").stream().forEach(name->testRefs.put(name, getTestActorRef(name)));
	}

	@AfterClass
	public void teardown() {
		TestKit.shutdownActorSystem(system, Duration.create("1 second"),false);
		system = null;
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
		return TestActorRef.create(this.system, Props.create(Orchestrator.class, () -> new Orchestrator(this.spatialContext,this.eventBus)), name);
	}
}
