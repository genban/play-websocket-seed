package sockets

import akka.actor._
import services.actors._
import services.messages._
import sockets.Protocol.Payload

/**
 * Created by peixiaobin on 2020/11/24.
 */
object UserWebSocket {

  def props(id: String, out: ActorRef): Props = Props(new UserWebSocket(id, out))
}

class UserWebSocket(id: String, out: ActorRef) extends Actor with ActorLogging {

  val notificationActor: ActorRef = NotificationActor.getRegion(context.system)

  notificationActor ! Envelope(id, EntityActor.Initialized)
  notificationActor ! Envelope(id, UserMessageActor.Connect(self))

  override def preStart(): Unit = {
    log.debug("UserWebSocket::{} is created", self)
  }

  override def receive: Receive = ({
    case inEvent: Payload =>
      log.debug("Received client message: {}", inEvent)
      out ! inEvent
  }:Receive) orElse NotificationProtocol.receive(out)

  override def postStop(): Unit = {
    log.debug(s"UserWebSocket::{} is removed", self)
  }
}
