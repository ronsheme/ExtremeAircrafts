package serialize

import java.nio.charset.Charset

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala._
import datatypes.PositionHeading
import org.apache.flink.streaming.util.serialization.{DeserializationSchema, SerializationSchema}

class PositionHeadingSchema extends DeserializationSchema[PositionHeading] with  SerializationSchema[PositionHeading]{
  val mapper = new ObjectMapper()

  implicit val typeInfo: TypeInformation[PositionHeading] = createTypeInformation[PositionHeading]

  override def isEndOfStream(t: PositionHeading): Boolean = {false;}

  override def deserialize(bytes: Array[Byte]): PositionHeading = mapper.readValue(bytes,classOf[PositionHeading])

  override def serialize(t: PositionHeading): Array[Byte] = {
    mapper.writeValueAsBytes(t)
  }

  override def getProducedType: TypeInformation[PositionHeading] = typeInfo
}
