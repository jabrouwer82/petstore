package jacob.recess.openapi

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    OpenapiServer.stream[IO].compile.drain.as(ExitCode.Success)
}