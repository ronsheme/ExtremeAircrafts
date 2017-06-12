package server;

import akkaLabs.ExtremeAircrafts.http.PositionChangedHttpEntity;
import akkaLabs.ExtremeAircrafts.position.Position;
import geojson.AircraftsGeoJSON;
import geojson.GeoJSONFeaturePoint;
import geojson.GeoJSONPoint;
import javafx.geometry.Pos;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by ron-lenovo on 5/28/2017.
 */

@Path("/aircrafts")
@Produces(MediaType.APPLICATION_JSON)
public class AircraftsResource {

    private AircraftsGeoJSON positions = new AircraftsGeoJSON();

    private Trailer trailer = new Trailer();

    public AircraftsResource(){

    }

    @GET
    public AircraftsGeoJSON getAircrafts() {
        return positions;
    }

    @POST
    @Path("/update")
    public void addPositionChanged(PositionChangedHttpEntity event){
        UUID uuid = event.getAircraftId();
        Position position = event.getPosition();
        Map<String,String> props = new HashMap<>();
        props.put("uuid",uuid.toString());
        this.positions.updateFeature(new GeoJSONFeaturePoint(new GeoJSONPoint(position.getLongitude(),position.getLatitude()),props));

        this.trailer.addTrail(uuid,position);
        this.positions.updateFeature(this.trailer.getAsGeoJSON(uuid));
    }
}
