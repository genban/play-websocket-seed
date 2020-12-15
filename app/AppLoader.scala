import play.api.Application
import play.api.ApplicationLoader.Context

/**
 * Created by peixiaobin on 2020/12/15.
 */
class AppLoader
  extends play.api.ApplicationLoader {

  def load(context: Context): Application = {
    new Components(context).application
  }
}
