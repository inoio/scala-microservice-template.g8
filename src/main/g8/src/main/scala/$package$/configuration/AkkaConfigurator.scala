package $package$.configuration

import com.typesafe.scalalogging.LazyLogging

class AkkaConfigurator extends LazyLogging {

  private val settings = Map(
    "akka.http.server.idle-timeout" -> "10 min",
    "akka.http.client.idle-timeout" -> "10 sec",
    "akka.loggers.0" -> "akka.event.slf4j.Slf4jLogger",
    "akka.loglevel" -> "INFO",
    "akka.logging-filter" -> "akka.event.slf4j.Slf4jLoggingFilter"
  )

  def configure(): Unit = {
    logger.info("configuring Akka via system props")
    settings.foreach{ case (key, value) =>
      logger.debug("key: "+key+", value: "+value)
      System.setProperty(key, value)
    }
  }

}
