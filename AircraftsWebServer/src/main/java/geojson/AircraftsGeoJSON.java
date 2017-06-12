package geojson;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by Ron on 10/06/2017.
 */
public class AircraftsGeoJSON extends GeoJSON {
    private Collection<GeoJSONFeature> features = new ConcurrentLinkedDeque<>();

    public AircraftsGeoJSON(){
        super("FeatureCollection");
    }

    public Collection<GeoJSONFeature> getFeatures(){return this.features;}

    public synchronized void updateFeature(GeoJSONFeature feature){
        Optional<GeoJSONFeature> optionalFeature = findFeatureByUUID(feature);
        if(optionalFeature.isPresent()){
            optionalFeature.get().setGeometry(feature.getGeometry());
        } else {
            this.features.add(feature);
        }
    }

    private Optional<GeoJSONFeature> findFeatureByUUID(GeoJSONFeature featureToMatch){
        return features.stream().filter(feature->feature.getClass() == featureToMatch.getClass() && feature.getProperties().get("uuid").equals(featureToMatch.getProperties().get("uuid"))).findFirst();
    }

}
