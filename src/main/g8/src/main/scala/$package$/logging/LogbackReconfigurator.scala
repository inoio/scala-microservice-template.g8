package $package$.logging

import java.io.InputStream

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.joran.JoranConfigurator
import ch.qos.logback.core.joran.spi.JoranException
import ch.qos.logback.core.util.StatusPrinter
import com.typesafe.scalalogging.LazyLogging
import org.slf4j.LoggerFactory


class LogbackReconfigurator(configResourcePath: ConfigResourcePath, loggerContextInfo: LoggerContextInfo) extends LazyLogging {

  private def enrich(context: LoggerContext): Unit = {
    logger.info("Enrich")
    context.putProperty("service_version", loggerContextInfo.gitHash)
    context.putProperty("service", loggerContextInfo.name)
  }

  private def reconfigure(context: LoggerContext, configFileStream: InputStream): Unit = {
    try {
      val configurator = new JoranConfigurator()
      configurator.setContext(context)
      context.reset()
      enrich(context)
      configurator.doConfigure(configFileStream)
    } catch {
      case _: JoranException => // StatusPrinter will handle this
    }
    StatusPrinter.printInCaseOfErrorsOrWarnings(context)
  }

  def configureLogging(): Unit = {
    LoggerFactory.getILoggerFactory match {
      case context: LoggerContext =>
        configResourcePath.path.flatMap { path =>
          Option(getClass.getResourceAsStream(path))
        } match {
          case Some(configFileStream) => reconfigure(context, configFileStream)
          case None => enrich(context)
        }
      case _ =>
        logger.warn("Current logging framework is not logback, cannot reconfigure.")
    }
  }
}
