package record

import java.util.Properties

import datatypes.PositionHeading
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import serialize.PositionHeadingSchema
import org.apache.flink.streaming.api.scala._
class Recorder {
  import Recorder._

  implicit val typeInfo: TypeInformation[PositionHeading] = createTypeInformation[PositionHeading]
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.getConfig.enableForceKryo()
  val messageStream: DataStream[PositionHeading] = env.addSource(new FlinkKafkaConsumer010[PositionHeading](TOPIC_NAME, new PositionHeadingSchema, props))
  messageStream.map(positionHeading => println(positionHeading.heading))
  env.execute()
}

object Recorder {
  val TOPIC_NAME = "position_updates"
  val HOST = "localhost"
  //address of vm that hosts kafka.
  val PORT = "9092" //address of vm that hosts kafka
  val props = new Properties()
  props.put("bootstrap.servers", s"$HOST:$PORT")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "serialize.PositionHeadingDeserializer")
  props.put("group.id", "flink-recorder")
  props.put("enable.auto.commit", "true")
}
