//package sockets
//
//import java.util.UUID
//import akka.actor.Actor.Receive
//import akka.actor.ActorRef
//import play.api.libs.json._
//import services.actors._
//
//import scala.util.Success
//
///**
// * Created by peixiaobin on 2020/11/24.
// */
//object ChatProtocol extends Protocol {
//
//  val name: String = "notification"
//
//  case class Notify(to: UUID, text: String, code: Int = 0, protocol: String = name) extends Protocol.Payload
//
//  object Notify {
//    implicit val jsonFormat: Format[Notify] = Json.format[Notify]
//  }
//
//
//  def receive(out: ActorRef, uid: UUID, chatActor: ActorRef): PartialFunction[Any, Unit] = {
//
//    case Success(Notify(to, text, _, _)) =>
//      if (text.nonEmpty)
//        chatActor ! Envelope(to, ChatMessage(to, uid, text))
//
//    case ChatMessage(_, from, text, _) =>
//      out ! ReceivedFrom(from, text)
//
//  }: Receive
//}
