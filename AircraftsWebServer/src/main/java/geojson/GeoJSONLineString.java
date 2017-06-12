package geojson;

import java.util.Collection;

/**
 * Created by Ron on 12/06/2017.
 */
public class GeoJSONLineString extends GeoJSON{

    private Collection<Double[]> coordinates;

    public GeoJSONLineString(Collection<Double[]> coordinates){
        super("LineString");
        this.coordinates = coordinates;
    }

    public Collection<Double[]> getCoordinates(){return this.coordinates;}
}
