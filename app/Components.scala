import controllers._
import play.api.ApplicationLoader.Context
import play.api.LoggerConfigurator
import play.api.Logging
import play.api.http._
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import router.Routes
import services.messages.NotificationActor

/**
 * Created by peixiaobin on 2020/12/15.
 */
class Components(context: Context)
  extends play.api.BuiltInComponentsFromContext(context) with AssetsComponents with Logging {

  LoggerConfigurator(environment.classLoader).foreach {
    _.configure(environment)
  }

  // Actors
  startActors()

  // Error Handler
  override lazy val httpErrorHandler = new HtmlOrJsonHttpErrorHandler(
    new DefaultHttpErrorHandler(environment, configuration, devContext.map(_.sourceMapper), Some(router)),
    new JsonHttpErrorHandler(environment, devContext.map(_.sourceMapper))
  )

  // controllers
  val homeController   = new HomeController(controllerComponents)
  val socketController = new WebSocketController(controllerComponents)(actorSystem, materializer, executionContext)
  val testController   = new TestController(controllerComponents)(actorSystem)

  override def router: Router = new Routes(httpErrorHandler, homeController, socketController, testController, assets)

  override def httpFilters: Seq[EssentialFilter] = Nil

  def startActors(): Unit = {
    //Start Actor ShardRegion
    NotificationActor.startRegion(configuration, actorSystem)
    logger.info("Actor ShardRegions have been started.")
  }

  logger.info("System has been started.")
}
