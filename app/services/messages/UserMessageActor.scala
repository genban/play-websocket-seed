package services.messages

import akka.actor._
import akka.routing._
import services.actors._

/**
 * Created by peixiaobin on 2020/12/15.
 */
object UserMessageActor {

  case class Connect(socket: ActorRef)
}

abstract class UserMessageActor extends EntityActor {

  var sockets: Router = Router(BroadcastRoutingLogic(), Vector())

  override def isIdle: Boolean = sockets.routees.isEmpty

  override def receive: Receive = ({
    case Envelope(id:String, c: UserMessageActor.Connect) =>
      log.debug(s"Socket ${c.socket.path} connected")
      sockets = sockets.addRoutee(c.socket)
      context watch c.socket
      self ! EntityActor.UnsetReceiveTimeout
      log.debug("{}, Now has {} sockets connected.", self.path.name, sockets.routees.length)

    case Terminated(a) =>
      log.debug("Socket {} Disconnected.", a.path)
      context unwatch a
      sockets = sockets.removeRoutee(a)
      if (isIdle) self ! EntityActor.SetReceiveTimeout
      log.debug("{}, Now has {} sockets connected.", self.path.name, sockets.routees.length)
  }: Receive) orElse super.receive
}