package position

import scala.math.{toRadians,toDegrees,sin,asin,cos,atan2,Pi}
/**
  * Created by Ron on 23/06/2017.
  */
object PositionUtil {
  def calculate(distance: Double, heading: Double, start: Position) = {
    val bearingRad = toRadians(heading)
    val latRad = toRadians(start.latitude)
    val lonRad = toRadians(start.longitude)
    val earthRadiusInMetres = 6371000
    val distFrac = distance / earthRadiusInMetres
    val latitudeResult = toDegrees(asin(Math.sin(latRad) * cos(distFrac) + cos(latRad) * sin(distFrac) * cos(bearingRad)))
    val a = atan2(sin(bearingRad) * sin(distFrac) * cos(latRad), cos(distFrac) - sin(latRad) * sin(latitudeResult))
    val longitudeResult = toDegrees((lonRad + a + 3 * Pi) % (2 * Pi) - Pi)
    new Position(longitudeResult, latitudeResult, start.altitude)
  }
}
