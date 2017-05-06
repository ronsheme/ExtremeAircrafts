package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircraftsCommand;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OrchestratorTest
{

	private static TestActorRef<Orchestrator> orch;

	@BeforeClass
	public static void SetUp()
	{
		Injector injector = Guice.createInjector(new ExtremeModule());
		ActorSystem sky = injector.getInstance(ActorSystem.class);
		orch = TestActorRef.create(sky, Props.create(Orchestrator.class), "orchestratosTest");
	}

	@Test
	public void createTest()
	{
		orch.tell(new ModifyAircraftsCommand(10), ActorRef.noSender());
		Assert.assertEquals(10, orch.underlyingActor().getAircrafts());
	}
}
