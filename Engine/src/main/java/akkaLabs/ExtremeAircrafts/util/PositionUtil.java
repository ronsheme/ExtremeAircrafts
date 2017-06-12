package akkaLabs.ExtremeAircrafts.util;

import akkaLabs.ExtremeAircrafts.position.Position;

/**
 * Created by Ron on 12/06/2017.
 */
public class PositionUtil {
    public static Position calculate(double distance,double heading,Position start){
        double brngRad = Math.toRadians(heading);
        double latRad = Math.toRadians(start.getLatitude());
        double lonRad = Math.toRadians(start.getLongitude());
        int earthRadiusInMetres = 6371000;
        double distFrac = distance/earthRadiusInMetres;

        double latitudeResult = Math.toDegrees(Math.asin(Math.sin(latRad) * Math.cos(distFrac) + Math.cos(latRad) * Math.sin(distFrac) * Math.cos(brngRad)));
        double a = Math.atan2(Math.sin(brngRad) * Math.sin(distFrac) * Math.cos(latRad), Math.cos(distFrac) - Math.sin(latRad) * Math.sin(latitudeResult));
        double longitudeResult =  Math.toDegrees((lonRad + a + 3 * Math.PI) % (2 * Math.PI) - Math.PI);

        return new Position(longitudeResult,latitudeResult,start.getAltitude());
    }
}
