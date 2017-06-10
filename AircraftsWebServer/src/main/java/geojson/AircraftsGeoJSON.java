package geojson;

import server.AircraftsResource;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Ron on 10/06/2017.
 */
public class AircraftsGeoJSON extends GeoJSON {
    private Collection<GeoJSONFeature> features = new LinkedList<>();

    public AircraftsGeoJSON(){
        this.type = "FeatureCollection";
    }

    public Collection<GeoJSONFeature> getFeatures(){return this.features;}

    public synchronized void addFeature(GeoJSONFeature feature){
        Optional<GeoJSONFeature> optionalFeature = findFeatureByUUID(feature.getProperties().get("uuid"));
        if(optionalFeature.isPresent()){
            optionalFeature.get().setGeometry(feature.getGeometry());
        } else {
            this.features.add(feature);
        }
    }

    private Optional<GeoJSONFeature> findFeatureByUUID(String uuid){
        return features.stream().filter(feature->feature.getProperties().get("uuid").equals(uuid)).findFirst();
    }

}
