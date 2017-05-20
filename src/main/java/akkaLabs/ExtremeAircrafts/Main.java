package akkaLabs.ExtremeAircrafts;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akkaLabs.ExtremeAircrafts.commands.aircraft.AdvanceMessage;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import akkaLabs.ExtremeAircrafts.http.AircraftsAggregator;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static akkaLabs.ExtremeAircrafts.ExtremeModule.UPDATE_RATE;

public class Main {
	private static final String ALL_AIRCRAFTS = "/user/" + ExtremeModule.ORCHESTRATOR + "/*";

	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(new ExtremeModule());
		ActorSystem sky = injector.getInstance(ActorSystem.class);
		ActorRef orchestrator = injector.getInstance(Key.get(ActorRef.class, Names.named(ExtremeModule.ORCHESTRATOR)));

		Http http = Http.get(sky);
		ActorMaterializer materializer = ActorMaterializer.create(sky);
		AircraftsAggregator aggregator = new AircraftsAggregator();
		Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = aggregator.createRoute().flow(sky, materializer);
		CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);
		System.out.println("Server online at http://localhost:8080/");

		orchestrator.tell(new ModifyAircrafts(10), ActorRef.noSender());
		sky.scheduler().schedule(Duration.Zero(), Duration.create(UPDATE_RATE, TimeUnit.SECONDS), () -> sky.actorSelection(ALL_AIRCRAFTS).tell(new AdvanceMessage(), ActorRef.noSender()), sky.dispatcher());

		System.out.println("Press any key to terminate...");
		System.in.read();
		binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> sky.terminate());
	}
}
