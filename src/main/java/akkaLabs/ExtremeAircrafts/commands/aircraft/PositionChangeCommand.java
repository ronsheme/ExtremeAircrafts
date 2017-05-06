package akkaLabs.ExtremeAircrafts.commands.aircraft;

import akkaLabs.ExtremeAircrafts.position.Position;

/**
 * @author royif
 * @since 06/05/17.
 */
public class PositionChangeCommand
{
	private Position destPosition;

	public PositionChangeCommand(Position destPosition)
	{
		this.destPosition = destPosition;
	}

	public Position getDestPosition()
	{
		return this.destPosition;
	}
}
