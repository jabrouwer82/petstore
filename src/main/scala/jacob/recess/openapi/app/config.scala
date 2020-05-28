package jacob.recess.openapi.app

import jacob.recess.openapi._
import eu.timepit.refined.pureconfig._
import pureconfig._
import pureconfig.generic.semiauto._

final case class ApiConfig(host: String, port: Port)

object ApiConfig {
  implicit val configReader: ConfigReader[ApiConfig] = deriveReader[ApiConfig]
}

final case class DbConfig(
  driver: String,
  url: String,
  user: String,
  pass: String,
)

object DbConfig {
  implicit val configReader: ConfigReader[DbConfig] = deriveReader[DbConfig]
}
