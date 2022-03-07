package $package$

import java.util.concurrent.ForkJoinPool

import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext
import com.softwaremill.macwire._
import $package$.configuration.{AkkaConfigurator, Environment}
import $package$.logging.LogbackReconfigurator
import $package$.logging.LoggingModule

trait DependencyInjectionModule extends LoggingModule  {
  val akkaConfigurator: AkkaConfigurator = wire[AkkaConfigurator]
  akkaConfigurator.configure()

  lazy val environment: Environment = wire[Environment]
  lazy val routes: Routes = wire[Routes]

  implicit def actorSystem: ActorSystem

  implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool(8))
}
