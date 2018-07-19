package $package$

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import fr.davit.akka.http.prometheus.scaladsl.server.HttpMetricsExports
import fr.davit.akka.http.prometheus.scaladsl.server.settings.HttpMetricsSettings
import $package$.configuration.{AkkaConfigurator, Environment}
import $package$.logging.LoggingModule
import io.prometheus.client.CollectorRegistry

trait DependencyInjectionModule extends LoggingModule  {

  lazy val environment = wire[Environment]

  lazy val routes = wire[Routes]
  lazy val akkaConfigurator = wire[AkkaConfigurator]

  def actorSystem: ActorSystem
  def actorMaterializer: ActorMaterializer

  // the custom prometheus registry that you use in your app
  val customCollectorRegistry = CollectorRegistry.defaultRegistry
  val httpMetricsExports = new HttpMetricsExports {
    override val registry = customCollectorRegistry
  }
  implicit val httpMetricsSettings = HttpMetricsSettings(
    exports = httpMetricsExports)
}
