package akkaLabs.ExtremeAircrafts.http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import akka.NotUsed;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.javadsl.Source;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvent;
import akka.http.javadsl.marshallers.jackson.Jackson;

public class AircraftsAggregator extends AllDirectives {

    private Map<UUID, PositionChangedEvent> positionEvents = new HashMap<>();

    //TODO get messages from all aircrafts and update the positions map

    public Route createRoute() {
        Map<UUID, PositionChangedEvent> tempMap = new HashMap<>(this.positionEvents);
        Source<PositionChangedEvent, NotUsed> positionStream = Source.fromIterator(() -> tempMap.values().iterator());
        return route(
                path("sky", () ->
                        get(() ->
                                complete(StatusCodes.OK, positionEvents, Jackson.marshaller()))));
    }
}
