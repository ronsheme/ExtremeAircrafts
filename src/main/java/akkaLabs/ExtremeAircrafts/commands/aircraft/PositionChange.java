package akkaLabs.ExtremeAircrafts.commands.aircraft;

import akkaLabs.ExtremeAircrafts.position.Position;

/**
 * @author royif
 * @since 06/05/17.
 */
public class PositionChange
{
	private Position destPosition;

	public PositionChange(Position destPosition)
	{
		this.destPosition = destPosition;
	}

	public Position getDestPosition()
	{
		return this.destPosition;
	}
}
