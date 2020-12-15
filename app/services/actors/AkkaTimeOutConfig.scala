package services.actors

import akka.util.Timeout
import play.api.Configuration

import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Created by peixiaobin on 2020/11/25.
 */
trait AkkaTimeOutConfig {

  def canonicalName: String

  def configuration: Configuration

  implicit lazy val timeout: Timeout = Timeout(
    configuration
      .getOptional[FiniteDuration](s"$canonicalName.akka.timeout")
      .getOrElse(5.seconds)
  )
}
