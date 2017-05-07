package akkaLabs.ExtremeAircrafts.messages.aircraft;

import akkaLabs.ExtremeAircrafts.position.Position;

import java.util.UUID;

import org.locationtech.spatial4j.shape.Point;

import akka.japi.Pair;

/**
 * @author royif
 * @since 06/05/17.
 */
public class AircraftPositionChangeEvent
{
	private UUID aircraftId;
	private Pair<Position,Point> location;

	public AircraftPositionChangeEvent(UUID aircraftId, Pair<Position,Point> location)
	{
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
}
