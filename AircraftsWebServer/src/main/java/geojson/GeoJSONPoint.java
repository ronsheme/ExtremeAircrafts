package geojson;

import java.util.Collections;

/**
 * Created by Ron on 10/06/2017.
 */
public class GeoJSONPoint extends GeoJSON{

    private double[] coordinates;

    public GeoJSONPoint(double x,double y){
        super("Point");
        this.coordinates = new double[]{x,y};
    }

    public double[] getCoordinates(){return this.coordinates;}
}
