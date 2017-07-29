package serialize

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import datatypes.PositionHeading
import org.apache.kafka.common.serialization.Deserializer

class PositionHeadingDeserializer extends Deserializer[PositionHeading] with Serializable{
  val mapper = new ObjectMapper()

  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def deserialize(s: String, bytes: Array[Byte]): PositionHeading = {
    mapper.readValue(bytes,classOf[PositionHeading])
  }

  override def close(): Unit = {}
}
