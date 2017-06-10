package geojson;

import server.AircraftsResource;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Ron on 10/06/2017.
 */
public class AircraftsGeoJSON extends GeoJSON {
    private Collection<AircraftsResource.GeoJSONFeature> features = new LinkedList<>();

    public AircraftsGeoJSON(){
        this.type = "FeatureCollection";
    }

    public Collection<AircraftsResource.GeoJSONFeature> getFeatures(){return this.features;}

    public void addFeature(AircraftsResource.GeoJSONFeature feature){this.features.add(feature);}

}
