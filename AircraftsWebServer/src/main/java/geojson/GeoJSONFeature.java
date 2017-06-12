package geojson;

import java.util.Map;

/**
 * Created by Ron on 12/06/2017.
 */
public class GeoJSONFeature extends GeoJSON {

    protected GeoJSON geometry;
    protected Map<String,String> properties;

    protected GeoJSONFeature(GeoJSON geometry, Map<String, String> properties) {
        super("Feature");
        this.geometry = geometry;
        this.properties = properties;
    }

    public GeoJSON getGeometry(){return this.geometry;}
    public Map<String, String> getProperties() {
        return properties;
    }
    public void setGeometry(GeoJSON geometry) {
        this.geometry = geometry;
    }

}
