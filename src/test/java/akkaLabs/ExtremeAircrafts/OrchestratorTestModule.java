package akkaLabs.ExtremeAircrafts;

import akka.actor.Props;
import akka.testkit.TestActorRef;

public class OrchestratorTestModule extends ExtremeModule{
	@Override
	protected void configure() {
		bind(TestActorRef.class).toProvider(() -> TestActorRef.create(this.sky,Props.create(Orchestrator.class, () -> new Orchestrator(this.spatialContext)), "orchestrator"));
	}

}
