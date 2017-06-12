package server;

import akkaLabs.ExtremeAircrafts.position.Position;
import geojson.GeoJSONFeatureLineString;
import geojson.GeoJSONLineString;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Ron on 12/06/2017.
 */
public class Trailer {

    private Map<UUID,CircularFifoQueue<Position>> trails = new ConcurrentHashMap<>();

    public void addTrail(UUID uuid,Position newPosition){
        if(!this.trails.containsKey(uuid)){
            this.trails.put(uuid,new CircularFifoQueue<>(30));
        }
        this.trails.get(uuid).add(newPosition);
    }

    public GeoJSONFeatureLineString getAsGeoJSON(UUID uuid){
        Map<String,String> props = new HashMap<>();
        props.put("uuid",uuid.toString());
        return new GeoJSONFeatureLineString(new GeoJSONLineString(trails.get(uuid).stream().map(position->new Double[]{position.getLongitude(),position.getLatitude()}).collect(Collectors.toList())),props);
    }
}
