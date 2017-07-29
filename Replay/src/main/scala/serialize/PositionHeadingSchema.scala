package serialize

import java.nio.charset.Charset

import datatypes.{Position, PositionHeading}
import org.apache.flink.api.common.typeinfo.{BasicTypeInfo, TypeInformation}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.util.serialization.{DeserializationSchema, SerializationSchema}
import rapture.json.jsonBackends.play._
import rapture.json._

class PositionHeadingSchema extends DeserializationSchema[PositionHeading] with  SerializationSchema[PositionHeading]{
  val deserializer = new PositionHeadingDeserializer
  override def isEndOfStream(t: PositionHeading): Boolean = {false;}

  override def deserialize(bytes: Array[Byte]): PositionHeading = deserializer.deserialize(null,bytes)

  override def serialize(t: PositionHeading): Array[Byte] = {
    val eventJson:Json = json"""{
                          "position": ${Json(t.position)},
                          "heading": ${t.heading}
                      }"""
    eventJson.toBareString.getBytes
  }

  override def getProducedType: TypeInformation[PositionHeading] = createTypeInformation[PositionHeading]

  implicit val positionSerializer: rapture.data.Serializer[Position, Json] =
    Json.serializer[Json].contramap {
      position: Position =>
        json"""{
              "longitude": ${position.longitude},
              "latitude": ${position.latitude},
              "altitude": ${position.altitude}
              }"""
    }
}
