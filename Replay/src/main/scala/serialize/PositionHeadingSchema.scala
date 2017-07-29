package serialize

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala._
import datatypes.PositionHeading
import org.apache.flink.streaming.util.serialization.{DeserializationSchema, SerializationSchema}
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}

class PositionHeadingSchema extends DeserializationSchema[PositionHeading] with  SerializationSchema[PositionHeading]{

  implicit val typeInfo: TypeInformation[PositionHeading] = createTypeInformation[PositionHeading]
  implicit lazy val formats = Serialization.formats(NoTypeHints)

  override def isEndOfStream(t: PositionHeading): Boolean = {false;}

  override def deserialize(bytes: Array[Byte]): PositionHeading = read[PositionHeading](new String(bytes))

  override def serialize(t: PositionHeading): Array[Byte] = {
    write(t).getBytes()
  }

  override def getProducedType: TypeInformation[PositionHeading] = typeInfo
}
