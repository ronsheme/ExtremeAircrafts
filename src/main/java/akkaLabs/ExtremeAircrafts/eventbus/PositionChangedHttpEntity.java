package akkaLabs.ExtremeAircrafts.eventbus;

import akkaLabs.ExtremeAircrafts.position.Position;

import java.util.UUID;

/**
 * Created by Ron on 21/05/2017.
 */
public class PositionChangedHttpEntity {
    private UUID aircraftId;
    private Position position;

    public PositionChangedHttpEntity(UUID aircraftId, Position position)
    {
        this.aircraftId = aircraftId;
        this.position = position;
    }

    public UUID getAircraftId()
    {
        return this.aircraftId;
    }

    public Position getPositoin()
    {
        return this.position;
    }
}
