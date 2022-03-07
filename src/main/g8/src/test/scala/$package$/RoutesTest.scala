package $package$

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.DurationInt

class RoutesTest
  extends AnyFlatSpec
    with Matchers
    with ScalatestRouteTest
    with TestDependencyInjectionModule {

  implicit def default(implicit system: ActorSystem): RouteTestTimeout = RouteTestTimeout(12.seconds)

  "The health endpoint" should "return HTTP OK" in {
    Get("/health") ~> routes.routes ~> check {
      status shouldEqual StatusCodes.OK
    }
  }
}
