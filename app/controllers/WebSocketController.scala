package controllers

import akka.actor._
import akka.stream._
import play.api.Logger
import play.api.libs.json._
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._
import sockets._

import scala.concurrent._
import scala.util.Try

/**
 * Provides a WebSocket controller.
 *
 * Created by peixiaobin on 2020/11/24.
 */
class WebSocketController(cc: ControllerComponents)(
  implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext
) extends AbstractController(cc) {

  val logger: Logger = play.api.Logger(getClass)

  import Protocol._

  implicit def userWebSocketMessageFlowTransformer: MessageFlowTransformer[Try[Payload], Payload] =
    caseClassMessageFlowTransformer[Payload, Payload]

  /**
   * Creates a websocket.  `accept` is preferable here because it returns a
   * Future[Flow], which is required internally.
   *
   * Configuring WebSocket Frame Length  -Dwebsocket.frame.maxLength=64k
   * @return a fully realized websocket.
   */
  def connect: WebSocket = WebSocket.acceptOrResult[Try[Payload], Payload] { request =>
    Future {
      request.getQueryString("userId") match {
        case Some(uid) => WebSocketFlow.actorRef[Try[Payload], Payload](out => UserWebSocket.props(uid, out), name = Some(uid))
        case None      => throw new Exception("No auth")
      }
    }.map(flow => Right(flow)).recover {
      case e: Exception =>
        logger.error("Cannot connection websocket", e)
        Left(BadRequest(Json.obj("error" -> "Cannot connection websocket")))
    }
  }
}
