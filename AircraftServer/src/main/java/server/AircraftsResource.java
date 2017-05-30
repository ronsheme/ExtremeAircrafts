package server;

import akka.http.javadsl.model.HttpEntity;
import akkaLabs.ExtremeAircrafts.http.PositionChangedHttpEntity;
import akkaLabs.ExtremeAircrafts.position.Position;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ron-lenovo on 5/28/2017.
 */

@Path("/aircrafts")
@Produces(MediaType.APPLICATION_JSON)
public class AircraftsResource {

    private ObjectMapper mapper = new ObjectMapper();

    private Map<UUID,Position> positionEvents = new ConcurrentHashMap<>();

    public AircraftsResource(){

    }

    @GET
    public Map<UUID,Position> getAircrafts() {
        return positionEvents;
    }

    @POST
    @Path("/update")
    public void addPositionChanged(HttpEntity.Strict event){
        String data = event.getData().decodeString(event.getContentType().getCharsetOption().get().nioCharset());
        try {
            PositionChangedHttpEntity entity = mapper.readValue(data,PositionChangedHttpEntity.class);
            this.positionEvents.put(entity.getAircraftId(),entity.getPositoin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
