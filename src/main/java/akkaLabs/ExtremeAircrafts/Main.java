package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akkaLabs.ExtremeAircrafts.commands.aircraft.AdvanceMessage;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import static akkaLabs.ExtremeAircrafts.ExtremeModule.UPDATE_RATE;

public class Main {
	private static final String ALL_AIRCRAFTS = "/user/" + ExtremeModule.ORCHESTRATOR + "/*";

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ExtremeModule());
		ActorSystem sky = injector.getInstance(ActorSystem.class);
		ActorRef orchestrator = injector.getInstance(Key.get(ActorRef.class, Names.named(ExtremeModule.ORCHESTRATOR)));

		orchestrator.tell(new ModifyAircrafts(10), ActorRef.noSender());
		sky.scheduler().schedule(Duration.Zero(), Duration.create(UPDATE_RATE, TimeUnit.SECONDS), () -> sky.actorSelection(ALL_AIRCRAFTS).tell(new AdvanceMessage(), ActorRef.noSender()), sky.dispatcher());

	}
}
