package $package$.configuration

class Environment() {
  import scala.util.Properties._
  val loggingFormat: String = envOrElse("LOGGING_FORMAT", "humanreadable")
  val hostIP: String = envOrElse("HOST_IP", "0.0.0.0")
  val hostPort: Int = envOrElse("HOST_PORT", "8080").toInt
}
