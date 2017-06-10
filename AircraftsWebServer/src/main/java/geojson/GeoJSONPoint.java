package geojson;

/**
 * Created by Ron on 10/06/2017.
 */
public class GeoJSONPoint extends GeoJSON{

    private double[] coordinates = new double[2];

    public GeoJSONPoint(double x,double y){
        this.type = "Point";
        coordinates[0] = x;
        coordinates[1] = y;
    }

    public double[] getCoordinates(){return this.coordinates;}
}
