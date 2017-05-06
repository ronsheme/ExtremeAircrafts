package akkaLabs.ExtremeAircrafts.position;

/**
 * @author royif
 * @since 06/05/17.
 */
public class Position
{
	private double latitude;
	private double longitude;
	private double altitude;

	public Position(double latitude, double longitude, double altitude)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	public double getLatitude()
	{
		return this.latitude;
	}

	public double getLongitude()
	{
		return this.longitude;
	}

	public double getAltitude()
	{
		return this.altitude;
	}

	@Override
	public String toString()
	{
		return "Position{" + "latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude + '}';
	}
}
