package $package$

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import $package$.configuration.Environment
import com.typesafe.scalalogging.LazyLogging

class Routes(actorSystem: ActorSystem,
             actorMaterializer: ActorMaterializer,
             environment: Environment)
  extends LazyLogging {

  implicit val ec = actorSystem
  implicit val amaterializer = actorMaterializer

  var on: Boolean = true


  val serviceRoutes: Route =
    pathSuffix("readinessProbe") {
      get {
        if(on) {
          complete {
            HttpEntity(ContentTypes.`application/json`, ByteString($package$.BuildInfo.toJson))
          }
        } else {
          complete(StatusCodes.ServiceUnavailable)
        }
      }
    } ~ path("health") {
      get {
        complete {
          HttpEntity(ContentTypes.`application/json`, ByteString($package$.BuildInfo.toJson))
        }
      }
    }

  val routes: Route =
      pathPrefix("$name$") {
        serviceRoutes
      } ~ pathPrefix("$vertical$" / "$name$") {
        serviceRoutes
      } ~ serviceRoutes

  val rejectRoute: Route = reject

  def shutdown(): Unit = on = false

}
