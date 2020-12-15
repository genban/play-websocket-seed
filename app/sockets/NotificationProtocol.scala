package sockets

import akka.actor.Actor.Receive
import akka.actor.ActorRef
import play.api.libs.json._

/**
 * Created by peixiaobin on 2020/11/24.
 */
object NotificationProtocol extends Protocol {

  val name: String = "notification"

  case class Notify(content: String, code: Int = 0, protocol: String = name) extends Protocol.Payload

  object Notify {
    implicit val jsonFormat: Format[Notify] = Json.format[Notify]
  }

  def receive(out: ActorRef): PartialFunction[Any, Unit] = {

    case notify: String =>
      out ! Notify(notify)

    case a: Any =>
      println("========Notification Unhandled Message=========")
      println(a)
  }: Receive
}
