package akkaLabs.ExtremeAircrafts;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ExtremeModule extends AbstractModule {

	@Override
	protected void configure() {
		ActorSystem sky = ActorSystem.create("Sky");
		bind(ActorSystem.class).toInstance(sky);
		bind(ActorRef.class).annotatedWith(Names.named("orchestrator"))
				.toProvider(() -> sky.actorOf(Props.create(Orchestrator.class), "orchestrator"));
	}

}
