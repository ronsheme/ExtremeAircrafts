package eventbus

import java.util.UUID

import akka.actor.ActorRef
import akka.event.{EventBus, LookupClassification}
import position.Position

final case class PositionEnvelope(topic: String, positionData: (UUID,Position,Double))

/**
  * Created by Ron on 23/06/2017.
  */
object PositionUpdateBus extends EventBus with LookupClassification{
  val POSITION_UPDATE_TOPIC: String = "PositionUpdateTopic"

  type Event = PositionEnvelope
  type Classifier = String
  type Subscriber = ActorRef

  override protected def classify(event: Event): Classifier = event.topic

  override protected def publish(event: Event, subscriber: Subscriber): Unit = subscriber ! event.positionData

  override protected def compareSubscribers(a: Subscriber, b: Subscriber): Int = a.compareTo(b)

  override protected def mapSize: Int = 1

  def subscribePositionUpdate(subscriber: ActorRef): Boolean = subscribe(subscriber,POSITION_UPDATE_TOPIC)
}
