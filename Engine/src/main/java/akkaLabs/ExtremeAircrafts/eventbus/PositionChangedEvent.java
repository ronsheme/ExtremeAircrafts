package akkaLabs.ExtremeAircrafts.eventbus;

import akka.japi.Pair;
import akkaLabs.ExtremeAircrafts.position.Position;
import org.locationtech.spatial4j.shape.Point;

import java.util.UUID;

/**
 * @author royif
 * @since 06/05/17.
 */
public class PositionChangedEvent
{
	private UUID aircraftId;
	private Pair<Position,Point> location;

	private double heading;

	public PositionChangedEvent(UUID aircraftId, Pair<Position, Point> location,double heading) {
		this.heading = heading;
		this.aircraftId = aircraftId;
		this.location = location;
	}

	public UUID getAircraftId()
	{
		return this.aircraftId;
	}

	public Pair<Position,Point> getLocation()
	{
		return this.location;
	}
	
	public Position getPosition(){
		return this.location.first();
	}
	
	public Point getPoint(){
		return this.location.second();
	}

	public double getHeading() {
		return heading;
	}
}
