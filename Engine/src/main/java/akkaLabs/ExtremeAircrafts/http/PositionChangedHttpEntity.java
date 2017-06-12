package akkaLabs.ExtremeAircrafts.http;

import akkaLabs.ExtremeAircrafts.position.Position;

import java.util.UUID;

/**
 * Created by Ron on 21/05/2017.
 *
 * The problem with PositionChangedEvent is that jackson cannot serialize because the Point has cyclic reference
 */
public class PositionChangedHttpEntity {
    public void setAircraftId(UUID aircraftId) {
        this.aircraftId = aircraftId;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private UUID aircraftId;
    private Position position;
    private double heading;

    public PositionChangedHttpEntity(){}

    public PositionChangedHttpEntity(UUID aircraftId, Position position,double heading) {
        this.heading = heading;
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

    public double getHeading() { return heading; }
}
