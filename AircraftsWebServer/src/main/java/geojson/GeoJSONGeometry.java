package geojson;

import java.util.Collection;

/**
 * Created by Ron on 12/06/2017.
 */
public class GeoJSONGeometry extends GeoJSON {

    private Collection<Double[]> coordinates;

    protected GeoJSONGeometry(String type,Collection<Double[]> coordinates) {
        super(type);
        this.coordinates = coordinates;
    }

    public Collection<Double[]> getCoordinates(){return this.coordinates;}
}
