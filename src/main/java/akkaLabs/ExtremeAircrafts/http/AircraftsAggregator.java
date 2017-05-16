package akkaLabs.ExtremeAircrafts.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import akka.NotUsed;
import akka.actor.AbstractActor.Receive;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import akkaLabs.ExtremeAircrafts.position.Position;

public class AircraftsAggregator extends AllDirectives{
	
	private Map<UUID,Position> positions = new HashMap<>();
	
	//TODO get messages from all aircrafts and update the positions map
	
	private Route createRoute() {
		    final Random rnd = new Random();
		    // streams are re-usable so we can define it here
		    // and use it for every request
		    Source<Integer, NotUsed> numbers = Source.fromIterator(() -> Stream.generate(rnd::nextInt).iterator());
		    //TODO change this into a stream of the arrived positions
		    return route(
		        path("random", () ->
		            get(() ->
		                complete(HttpEntities.create(ContentTypes.TEXT_PLAIN_UTF8,
		                    // transform each number to a chunk of bytes
		                    numbers.map(x -> ByteString.fromString(x + "\n")))))));
		  }
}
