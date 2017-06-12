package geojson;

import server.AircraftsResource;

import java.util.Map;

/**
 * Created by Ron on 10/06/2017.
 */
public class GeoJSONFeaturePoint extends GeoJSONFeature{

    public GeoJSONFeaturePoint(GeoJSONPoint point, Map<String,String> properties){
        super(point,properties);
    }
    public void setGeometry(GeoJSONPoint geometry) {
        this.geometry = geometry;
    }
}
