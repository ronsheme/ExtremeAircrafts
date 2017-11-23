import org.opensky.api.OpenSkyApi

object Test {
  def main(args: Array[String]): Unit = {
    val openskyApi = new OpenSkyApi
    val states = openskyApi.getStates(0, null)
    println(states)
  }
}
