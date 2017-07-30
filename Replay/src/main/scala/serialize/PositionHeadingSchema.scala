package serialize

import java.util.UUID

import datatypes.PositionHeading
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.read
import org.apache.flink.streaming.api.scala._

class PositionHeadingSchema extends KeyedDeserializationSchema[Tuple2[UUID,PositionHeading]]{
  implicit lazy val formats = Serialization.formats(NoTypeHints)
  val typeInfo = createTypeInformation[Tuple2[UUID,PositionHeading]]

  override def deserialize(keyBytes: Array[Byte], messageBytes: Array[Byte], topic: String, partition: Int, offset: Long): Tuple2[UUID,PositionHeading] = {
    val key = UUID.nameUUIDFromBytes(keyBytes)
    val msg = read[PositionHeading](new String(messageBytes))
    new Tuple2[UUID,PositionHeading](key,msg)
  }

  override def isEndOfStream(nextElement: (UUID, PositionHeading)): Boolean = false

  override def getProducedType: TypeInformation[(UUID, PositionHeading)] = typeInfo
}
