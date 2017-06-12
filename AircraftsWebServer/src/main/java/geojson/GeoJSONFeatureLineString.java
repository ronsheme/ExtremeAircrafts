package geojson;

import java.util.Map;

/**
 * Created by Ron on 12/06/2017.
 */
public class GeoJSONFeatureLineString extends GeoJSONFeature {

    public GeoJSONFeatureLineString(GeoJSONLineString lineString, Map<String,String> properties){
        super(lineString,properties);
    }

    public void setGeometry(GeoJSONLineString lineString) {
        this.geometry = lineString;
    }
}
