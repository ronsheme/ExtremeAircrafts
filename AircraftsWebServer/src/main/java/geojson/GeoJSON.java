package geojson;

import java.util.Map;

/**
 * Created by Ron on 10/06/2017.
 */
public class GeoJSON{
    protected String type;

    protected GeoJSON(String type){
        this.type = type;
    }

    public String getType(){return this.type;}
}