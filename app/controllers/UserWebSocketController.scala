package controllers

import akka.actor._
import akka.stream._
import play.api.Logger
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import play.api.mvc.WebSocket.MessageFlowTransformer
import play.api.mvc._
import sockets._

import scala.concurrent._
import scala.util.Try

/**
 * Provides a websocket controller.
 *
 * Created by peixiaobin on 2020/11/24.
 */
class UserWebSocketController(cc: ControllerComponents)(
  implicit system: ActorSystem,
  mat: Materializer,
  ec: ExecutionContext
) extends AbstractController(cc) {

  val logger: Logger = play.api.Logger(getClass)

  import Protocol._

  implicit def userWebSocketMessageFlowTransformer: MessageFlowTransformer[Try[Payload], Payload] =
    caseClassMessageFlowTransformer[Payload, Payload]

  /**
   * Creates a websocket.  `accept` is preferable here because it returns a
   * Future[Flow], which is required internally.
   *
   * @return a fully realized websocket.
   */
  def connect: WebSocket = WebSocket.acceptOrResult[Try[Payload], Payload] { request =>
    logger.info(s"'ws' ${request.id} function is called")
    Future {
      WebSocketFlow.actorRef[Try[Payload], Payload](out => UserWebSocket.props(request.id.toString, out))
    }.map(flow => Right(flow)).recover {
      case e: Exception =>
        logger.error("Cannot create websocket", e)
        Left(BadRequest(Json.obj("error" -> "Cannot create websocket")))
    }
  }
}
