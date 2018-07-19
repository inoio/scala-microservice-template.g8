package $package$.logging

import $package$.BuildInfo
import $package$.configuration.Environment
import com.softwaremill.macwire._

trait LoggingModule {
  protected def environment: Environment
  protected lazy val configResourcePath = new ConfigResourcePath {
    override def path: Option[String] = Some("/logback-"+environment.loggingFormat+".xml")
  }
  protected lazy val buildInfo = BuildInfo
  protected lazy val logbackReconfigurator: LogbackReconfigurator = wire[LogbackReconfigurator]
}