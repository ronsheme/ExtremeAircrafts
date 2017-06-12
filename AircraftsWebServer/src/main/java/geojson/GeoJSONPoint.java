package geojson;

import java.util.Collections;

/**
 * Created by Ron on 10/06/2017.
 */
public class GeoJSONPoint extends GeoJSONGeometry{

    public GeoJSONPoint(double x,double y){
        super("Point", Collections.singleton(new Double[]{x,y}));
    }
}
