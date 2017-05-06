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

public class OrchestratorTest {

	private static ActorSystem sky;

	private static TestActorRef<Orchestrator> getTestActorRef(String name){
		return TestActorRef.create(sky, Props.create(Orchestrator.class),name);
	}
	
	@BeforeClass
	public static void SetUp() {
		Injector injector = Guice.createInjector(new ExtremeModule());
		sky = injector.getInstance(ActorSystem.class);
	}

	@Test
	public void createTest() {
		TestActorRef<Orchestrator> orch = getTestActorRef("createTest");
		orch.tell(new ModifyAircraftsCommand(10), ActorRef.noSender());
		Assert.assertEquals(10, orch.underlyingActor().getAircrafts());
	}

	@Test
	public void addTest() {
		TestActorRef<Orchestrator> orch = getTestActorRef("addTest");
		orch.tell(new ModifyAircraftsCommand(10), ActorRef.noSender());
		orch.tell(new ModifyAircraftsCommand(15), ActorRef.noSender());
		Assert.assertEquals(15, orch.underlyingActor().getAircrafts());
	}
	
	@Test
	public void removeTest() {
		TestActorRef<Orchestrator> orch = getTestActorRef("removeTest");
		orch.tell(new ModifyAircraftsCommand(10), ActorRef.noSender());
		orch.tell(new ModifyAircraftsCommand(5), ActorRef.noSender());
		Assert.assertEquals(5, orch.underlyingActor().getAircrafts());
	}
}
