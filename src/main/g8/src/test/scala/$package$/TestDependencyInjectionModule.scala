package $package$

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import $package$.configuration.Environment
import org.scalamock.scalatest.MockFactory

import scala.concurrent.ExecutionContext
import scala.util.Properties.envOrElse

trait TestDependencyInjectionModule extends DependencyInjectionModule with MockFactory { self: ScalatestRouteTest =>
  def actorSystem: ActorSystem = implicitly[ActorSystem]

  override lazy val environment = new TestEnvironment()
  class TestEnvironment extends Environment {
  }
}
