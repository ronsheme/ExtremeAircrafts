package akkaLabs.ExtremeAircrafts.messages.aircraft;

import akkaLabs.ExtremeAircrafts.position.Position;

import java.util.UUID;

/**
 * @author royif
 * @since 06/05/17.
 */
public class AircraftPositionChangeEvent
{
	private UUID aircraftId;
	private Position position;

	public AircraftPositionChangeEvent(UUID aircraftId, Position position)
	{
		this.aircraftId = aircraftId;
		this.position = position;
	}

	public UUID getAircraftId()
	{
		return this.aircraftId;
	}

	public Position getPosition()
	{
		return this.position;
	}
}
