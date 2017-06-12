package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.japi.LookupEventBus;
import akkaLabs.ExtremeAircrafts.commands.aircraft.AdvanceMessage;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvelope;
import akkaLabs.ExtremeAircrafts.http.HttpUpdater;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static akkaLabs.ExtremeAircrafts.ExtremeModule.UPDATE_RATE;

public class Main {
	private static final String ALL_AIRCRAFTS = "/user/" + ExtremeModule.ORCHESTRATOR + "/*";

	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(new ExtremeModule());
		ActorSystem sky = injector.getInstance(ActorSystem.class);
		ActorRef orchestrator = injector.getInstance(Key.get(ActorRef.class, Names.named(ExtremeModule.ORCHESTRATOR)));//TODO: use injection that is supported by akka
		LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus = injector.getInstance(Key.get(LookupEventBus.class, Names.named(ExtremeModule.EVENTBUS_NAME)));//TODO: use injection that is supported by akka

//		Http http = Http.get(sky);
//		ActorMaterializer materializer = ActorMaterializer.create(sky);
//		AircraftsServer aggregator = new AircraftsServer();
//		Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = aggregator.createRoute().flow(sky, materializer);
//		CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);
//		System.out.println("Server online at http://localhost:8080/");

		sky.actorOf(Props.create(HttpUpdater.class,()->new HttpUpdater(eventBus)),"httpUpdater");

		orchestrator.tell(new ModifyAircrafts(50), ActorRef.noSender());
		sky.scheduler().schedule(Duration.Zero(), Duration.create(UPDATE_RATE, TimeUnit.SECONDS), () -> sky.actorSelection(ALL_AIRCRAFTS).tell(new AdvanceMessage(), ActorRef.noSender()), sky.dispatcher());

//		System.out.println("Press any key to terminate...");
//		System.in.read();
//		binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> sky.terminate());
	}
}
