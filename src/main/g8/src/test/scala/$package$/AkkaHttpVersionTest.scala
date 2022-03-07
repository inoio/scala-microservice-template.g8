package $package$

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ManifestInfo
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AkkaHttpVersionTest extends AnyFlatSpec with ScalatestRouteTest with Matchers {
  "The Akka HTTP versions" should "match" in {
    ManifestInfo(implicitly[ActorSystem]).checkSameVersion(
      "Akka HTTP",
      List(
        "akka-parsing",
        "akka-http-core",
        "akka-http2-support",
        "akka-http",
        "akka-http-caching",
        "akka-http-testkit",
        "akka-http-tests",
        "akka-http-marshallers-scala",
        "akka-http-marshallers-java",
        "akka-http-spray-json",
        "akka-http-xml",
        "akka-http-jackson"
      ),
      logWarning = true
    )
  }
}
