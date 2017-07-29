package serialize

import java.util

import datatypes.PositionHeading
import org.apache.kafka.common.serialization.Deserializer
import org.json4s.{DefaultFormats, NoTypeHints}
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read

class PositionHeadingDeserializer extends Deserializer[PositionHeading] with Serializable{
  implicit lazy val formats = Serialization.formats(NoTypeHints)

  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def deserialize(s: String, bytes: Array[Byte]): PositionHeading = {
    read[PositionHeading](new String(bytes))
  }

  override def close(): Unit = {}
}
