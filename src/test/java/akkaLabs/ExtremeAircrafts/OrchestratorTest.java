package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import com.google.inject.Inject;
import org.locationtech.spatial4j.context.SpatialContext;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

@Guice(modules = ExtremeModule.class)
public class OrchestratorTest {

	@Inject
	private static ActorSystem sky;
	@Inject
	private static SpatialContext spatialContext;

	@Test
	public void createTest() {
		TestActorRef<Orchestrator> orch = getTestActorRef("createTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		Assert.assertEquals(10, orch.underlyingActor().getAircraftsCount());
	}

	@Test
	public void addTest() {
		TestActorRef<Orchestrator> orch = getTestActorRef("addTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		orch.tell(new ModifyAircrafts(15), ActorRef.noSender());
		Assert.assertEquals(15, orch.underlyingActor().getAircraftsCount());
	}

	@Test
	public void removeTest() {
		TestActorRef<Orchestrator> orch = getTestActorRef("removeTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		orch.tell(new ModifyAircrafts(5), ActorRef.noSender());
		Assert.assertEquals(5, orch.underlyingActor().getAircraftsCount());
	}

	private TestActorRef<Orchestrator> getTestActorRef(String name) {
		return TestActorRef.create(sky, Props.create(Orchestrator.class, () -> new Orchestrator(spatialContext)), name);
	}
}
