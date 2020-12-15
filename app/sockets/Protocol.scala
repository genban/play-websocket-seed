package sockets

import play.api.libs.json._

/**
 * Created by peixiaobin on 2020/11/24.
 */
trait Protocol {

  def name: String

  def protocolError: JsError = JsError(s"error.ws.protocol.$name")
}

/**
 * WebSocket 协议
 */
object Protocol {

  /**
   * 消息载体
   */
  trait Payload {

    def code: Int
    def protocol: String
  }

  implicit val payloadFormat: Format[Payload] = new Format[Payload] {
    override def reads(json: JsValue): JsResult[Payload] = (json \ "protocol").validate[String] match {
      case JsSuccess(NotificationProtocol.name, _) => NotificationProtocol.Notify.jsonFormat.reads(json)
      case _                                       => unknownProtocolError
    }

    override def writes(o: Payload): JsValue = o match {
      case n: NotificationProtocol.Notify => NotificationProtocol.Notify.jsonFormat.writes(n)
      case _                              => JsNull
    }
  }

//  /**
//   * 消息载体
//   */
//  case class Payload(code: Int, protocol: String)
//
//  object Payload {
//    implicit val payloadFormat: Format[Payload] = Json.format[Payload]
//  }

  def unknownProtocolError: JsError = JsError(s"error.ws.protocol.unknown")
}
