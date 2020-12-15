package controllers

import play.api.mvc._

/**
 * Created by peixiaobin on 2020/11/24.
 */
class HomeController(cc: ControllerComponents) extends AbstractController(cc) {

  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }
}
