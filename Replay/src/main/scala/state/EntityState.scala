package state

import java.util.UUID

import datatypes.PositionHeading
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.configuration.Configuration

class EntityState extends RichMapFunction[(UUID,PositionHeading),(UUID,PositionHeading)]{

  private var numericalState: MapState[String,Double] = _

  override def map(value: (UUID, PositionHeading)): (UUID,PositionHeading) = {
    numericalState.put("heading",value._2.heading)
    numericalState.put("altitude",value._2.position.altitude)
    numericalState.put("latitude",value._2.position.latitude)
    numericalState.put("longitude",value._2.position.longitude)
    value
  }

  override def open(parameters: Configuration): Unit = {
    numericalState = getRuntimeContext.getMapState(new MapStateDescriptor("updateEntityRepresentation",classOf[String],classOf[Double]))
  }
}
