package $package$

import java.util.concurrent.atomic.AtomicBoolean
import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import $package$.configuration.Environment
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.ExecutionContext


class Routes(actorSystem: ActorSystem,
             environment: Environment)
  extends LazyLogging {

  implicit val ec: ActorSystem = actorSystem
  implicit val executionContext: ExecutionContext = actorSystem.dispatcher

  val on: AtomicBoolean = new AtomicBoolean(true)

  val serviceRoutes: Route =
    pathSuffix("readinessProbe") {
      get {
        if(on.get) {
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

  def shutdown(): Unit = on.set(false)

}
