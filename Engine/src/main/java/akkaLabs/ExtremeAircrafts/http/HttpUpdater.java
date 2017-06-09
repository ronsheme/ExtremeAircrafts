package akkaLabs.ExtremeAircrafts.http;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.event.japi.LookupEventBus;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akkaLabs.ExtremeAircrafts.ExtremeModule;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvelope;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvent;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEventBus;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Ron on 20/05/2017.
 */
public class HttpUpdater extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    private LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus;
    private ActorSystem system;
    private Materializer materializer;
    private String aircraftAddress;
    private ObjectMapper mapper = new ObjectMapper();

    public HttpUpdater(LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus) {
        this.eventBus = eventBus;
        this.aircraftAddress = ExtremeModule.AIRCRAFT_SERVER_ADDRESS;
        this.system = this.getContext().getSystem();
        this.materializer = ActorMaterializer.create(this.system);
        this.eventBus.subscribe(this.getSelf(), PositionChangedEventBus.POSITION_CHANGED_TOPIC);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(PositionChangedEvent.class, evt -> {
            logger.info("Sending HTTP request with PositionChangedEvent for aircraft {}", evt.getAircraftId());
            Http http = Http.get(system);
            HttpRequest request = HttpRequest.POST(this.aircraftAddress).withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON,
                mapper.writeValueAsString(new PositionChangedHttpEntity(evt.getAircraftId(), evt.getPosition()))));
            http.singleRequest(request, materializer);
        }).build();
    }
}
