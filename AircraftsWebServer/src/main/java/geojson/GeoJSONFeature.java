package geojson;

import server.AircraftsResource;

import java.util.Map;

/**
 * Created by Ron on 10/06/2017.
 */
public class GeoJSONFeature extends GeoJSON{
    private Map<String,String> properties;



    private GeoJSONPoint geometry;

    public GeoJSONFeature(GeoJSONPoint point, Map<String,String> properties){
        this.type = "Feature";
        this.geometry = point;
        this.properties = properties;
    }
    public Map<String, String> getProperties() {
        return properties;
    }
    public GeoJSONPoint getGeometry(){return this.geometry;}
    public void setGeometry(GeoJSONPoint geometry) {
        this.geometry = geometry;
    }
}
