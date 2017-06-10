package server;

import akkaLabs.ExtremeAircrafts.http.PositionChangedHttpEntity;
import akkaLabs.ExtremeAircrafts.position.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import geojson.AircraftsGeoJSON;
import geojson.GeoJSON;
import geojson.GeoJSONFeature;
import geojson.GeoJSONPoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ron-lenovo on 5/28/2017.
 */

@Path("/aircrafts")
@Produces(MediaType.APPLICATION_JSON)
public class AircraftsResource {

    private ObjectMapper mapper = new ObjectMapper();

    private AircraftsGeoJSON positions = new AircraftsGeoJSON();

    public AircraftsResource(){

    }

    @GET
    public AircraftsGeoJSON getAircrafts() {
        return positions;
    }

    @POST
    @Path("/update")
    public void addPositionChanged(PositionChangedHttpEntity event){
        Map<String,String> features = new HashMap<>();
        features.put("uuid",event.getAircraftId().toString());
        this.positions.addFeature(new GeoJSONFeature(new GeoJSONPoint(event.getPosition().getLongitude(),event.getPosition().getLatitude()),features));
    }
}
