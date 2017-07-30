package record

import java.util.{Properties, UUID}

import datatypes.PositionHeading
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.api.scala._
import serialize.PositionHeadingSchema
import state.EntityState
class Recorder {
  import Recorder._

  implicit val typeInfo: TypeInformation[PositionHeading] = createTypeInformation[PositionHeading]
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
  env.getConfig.enableForceKryo()
  val messageStream: DataStream[Tuple2[UUID,PositionHeading]] = env.addSource(new FlinkKafkaConsumer010(TOPIC_NAME, new PositionHeadingSchema, props))
  messageStream.keyBy(0).map(new EntityState())
  env.enableCheckpointing(1000)
  env.setStateBackend(new FsStateBackend("file:///tmp/checkpoints",true))
  env.execute("extremeAircraftsRecorder")
}

object Recorder {
  val TOPIC_NAME = "position_updates"
  val HOST = "localhost"
  //address of vm that hosts kafka.
  val PORT = "9092" //address of vm that hosts kafka
  val props = new Properties()
  props.put("bootstrap.servers", s"$HOST:$PORT")
  props.put("group.id", "flink-recorder")
  props.put("enable.auto.commit", "true")
}
