package datatypes

/**
  * Created by Ron on 23/06/2017.
  */
class Position(val longitude: Double = 0,val latitude: Double = 0,val altitude: Double = 0) {
    override def toString: String = s"{lat=$latitude, long=$longitude, alt=$altitude}"
}
