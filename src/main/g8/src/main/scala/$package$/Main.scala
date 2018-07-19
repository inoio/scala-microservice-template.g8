package $package$

import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse}
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.{
  complete,
  extractUri
}
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import akka.actor.{ActorSystem, CoordinatedShutdown}
import com.typesafe.scalalogging.LazyLogging
import scala.util.{Failure, Success}
import fr.davit.akka.http.prometheus.scaladsl.server.HttpMetricsRoute._
import fr.davit.akka.http.prometheus.scaladsl.server.HttpMetricsDirectives._
import io.prometheus.client.hotspot.DefaultExports

object Main extends App with DependencyInjectionModule with LazyLogging  {
  implicit lazy val actorSystem = ActorSystem("system")
  implicit lazy val actorMaterializer = ActorMaterializer()
  implicit lazy val executionContext = actorSystem.dispatcher

  logbackReconfigurator.configureLogging()
  akkaConfigurator.configure()
  DefaultExports.initialize()

  val (host, port) = (environment.hostIP, environment.hostPort)
  val bindingFuture =
    Http().bindAndHandle(handleExceptions()(withMetrics(httpMetricsExports) {
      routes.routes
    }), host, port)

  Http().bindAndHandle(routes.rejectRoute.withMetricsHandler, host, port + 1)

  def handleExceptions(): (Route) => Route = (route: Route) => {
    val exceptionHandler: ExceptionHandler = ExceptionHandler {
      case e: Exception =>
        extractUri { uri =>
          logger.error("error in route processing for request "+uri.toString ,  e)
          complete(HttpResponse(InternalServerError, entity = "There was an internal server error, please try again later."))
        }
    }
    akka.http.scaladsl.server.Directives.handleExceptions(exceptionHandler) {
        route
    }
  }


  bindingFuture.onComplete {
    case Success(_) =>
      println(s"Server for started at localhost:8080")
    case Failure(cause) =>
      println(s"Server failed to start", cause)
  }

  CoordinatedShutdown(actorSystem).addTask(
    CoordinatedShutdown.PhaseBeforeServiceUnbind, "http_shutdown") { () =>
    routes.shutdown()
    logger.info(s"Server is Shuting down, cause (external signal), wait 4.8 Seconds to complete requests")
    Thread.sleep(4800)
    logger.info(s"Going  down")
    bindingFuture.flatMap(_.unbind).flatMap { _ =>
      Http().shutdownAllConnectionPools
    }.map { _ =>
      Done
    }
  }

}
