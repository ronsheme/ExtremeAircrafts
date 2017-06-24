package http;

/**
 * @author royif
 * @since 06/05/17.
 */
@Deprecated
public class Position
{
	private double latitude;
	private double longitude;
	private double altitude;

	public Position()
	{
		this(0, 0, 0);
	}

	public Position(double longitude, double latitude, double altitude)
	{
		this.longitude = longitude;
		this.latitude = latitude;
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
		return "{lat=" + latitude + ", long=" + longitude + ", alt=" + altitude + "}";
	}
}
