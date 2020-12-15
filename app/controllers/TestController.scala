package controllers

import akka.actor.ActorSystem
import play.api.mvc._
import services.actors.Envelope
import services.messages.NotificationRegionComponents
import sockets.NotificationProtocol.Notify

import play.api.libs.json._

/**
 * Created by peixiaobin on 2020/11/24.
 */
class TestController(cc: ControllerComponents)(implicit val actorSystem: ActorSystem ) extends AbstractController(cc) with NotificationRegionComponents {

  def broadcast: Action[JsValue] = Action(parse.json) { implicit req =>
    val allUsers = (req.body \ "userIds").as[Seq[String]]
    allUsers foreach { uid =>
      _notificationRegion ! Envelope(uid, "这是有又前进了一步")
    }
    Ok(Json.toJson("message" -> "success"))
  }
}
