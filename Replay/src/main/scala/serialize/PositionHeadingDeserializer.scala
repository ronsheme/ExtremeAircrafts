package serialize

import java.nio.charset.Charset
import java.util

import datatypes.{Position, PositionHeading}
import org.apache.kafka.common.serialization.Deserializer
import rapture.json.jsonBackends.play._
import rapture.json._

class PositionHeadingDeserializer extends Deserializer[PositionHeading]{
  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def deserialize(s: String, bytes: Array[Byte]): PositionHeading = {
    val json: Json = Json.parse(new String(bytes,Charset.defaultCharset()))
    new PositionHeading(json.position.as[Position],json.heading.as[Double])
  }

  override def close(): Unit = {}
}
