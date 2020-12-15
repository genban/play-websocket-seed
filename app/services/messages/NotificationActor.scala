package services.messages

import akka.actor._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import services.actors._

/**
 * Created by peixiaobin on 2020/12/15.
 */
object NotificationActor extends AkkaClusterSharding {

  val shardName: String = "notification_actors"

  override def props: Props = Props(new NotificationActor())
}

class NotificationActor extends UserMessageActor {

  override def receive: Receive = ({

    case Envelope(_, notify: String) =>
      sockets.route(notify, sender())

  }: Receive) orElse super.receive

  override def postStop(): Unit = println(s"${DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss").print(DateTime.now)} PRINTLN [${self.path}]is stoped")
}

trait NotificationRegionComponents {
  // self: AkkaTimeOutConfig =>

  def actorSystem: ActorSystem

  def _notificationRegion: ActorRef = NotificationActor.getRegion(actorSystem)
}