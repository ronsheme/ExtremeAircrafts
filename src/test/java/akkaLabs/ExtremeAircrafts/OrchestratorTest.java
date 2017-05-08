package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

public class OrchestratorTest
{

	private static ActorSystem sky;
	private static Injector injector;

	private static TestActorRef<Orchestrator> getTestActorRef(String name)
	{
		return TestActorRef.create(sky, Props.create(Orchestrator.class, () -> new Orchestrator(injector.getInstance(SpatialContext.class))), name);
	}

	@BeforeClass
	public static void SetUp()
	{
		injector = Guice.createInjector(new ExtremeModule());
		sky = injector.getInstance(ActorSystem.class);
	}

	@Test
	public void createTest()
	{
		TestActorRef<Orchestrator> orch = getTestActorRef("createTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		Assert.assertEquals(10, orch.underlyingActor().getAircraftsCount());
	}

	@Test
	public void addTest()
	{
		TestActorRef<Orchestrator> orch = getTestActorRef("addTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		orch.tell(new ModifyAircrafts(15), ActorRef.noSender());
		Assert.assertEquals(15, orch.underlyingActor().getAircraftsCount());
	}

	@Test
	public void removeTest()
	{
		TestActorRef<Orchestrator> orch = getTestActorRef("removeTest");
		orch.tell(new ModifyAircrafts(10), ActorRef.noSender());
		orch.tell(new ModifyAircrafts(5), ActorRef.noSender());
		Assert.assertEquals(5, orch.underlyingActor().getAircraftsCount());
	}
}
