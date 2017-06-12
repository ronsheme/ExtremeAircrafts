package geojson;

import java.util.Collection;

/**
 * Created by Ron on 12/06/2017.
 */
public class GeoJSONLineString extends GeoJSONGeometry{

    public GeoJSONLineString(Collection<Double[]> coordinates){
        super("LineString",coordinates);
    }
}
