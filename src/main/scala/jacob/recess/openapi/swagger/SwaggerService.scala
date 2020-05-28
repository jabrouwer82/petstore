package jacob.recess.openapi.swagger

import cats.implicits._
import cats.effect._
import eu.timepit.refined.auto._
import jacob.recess.openapi.app._
import jacob.recess.openapi.pet._
import jacob.recess.openapi.purchase._
import jacob.recess.openapi.customer._
import org.http4s.implicits._
import org.http4s.server.blaze._
import pureconfig._
import scala.concurrent.ExecutionContext.global
import scala.io._
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.swagger.http4s._

object SwaggerService extends IOApp {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def run(args: List[String]): IO[ExitCode] = {
    val server = for {
      configSrc <- IO(ConfigSource.default)
      apiConf = configSrc.at("swagger").loadOrThrow[ApiConfig]

      server <- getServer(apiConf)
    } yield server

    server.attempt.unsafeRunSync() match {
      case Left(e) =>
        IO {
          println("***** Something went wrong *****")
          Option(e) match {
            case Some(m) => println(m.getMessage)
            case None => println("No error message")
          }
          ExitCode.Error
        }
      case Right(r) => IO.pure(r)
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def getServer(apiConf: ApiConfig): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(apiConf.port, apiConf.host)
      .withHttpApp(
        new SwaggerHttp4s(
          List(
            PetApi.endpoints,
            CustomerApi.endpoints,
            PurchaseApi.endpoints,
          ).foldK.toOpenAPI("Pet Store Combined Api", "0.1.0").toYaml
        ).routes.orNotFound
      )
      .resource
      .use(_ => IO(StdIn.readLine()))
      .as(ExitCode.Success)

}
