package jacob.recess.openapi.pet

import cats.effect._
import cats.implicits._
import doobie.quill._
import doobie.util.transactor._
import eu.timepit.refined.auto._
import io.getquill._
import jacob.recess.openapi.app._
import jacob.recess.openapi.database._
import org.http4s.implicits._
import org.http4s.server.blaze._
import pureconfig._
import scala.concurrent.ExecutionContext.global
import scala.io._
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.swagger.http4s._

object PetService extends IOApp {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def run(args: List[String]): IO[ExitCode] = {
    val server = for {
      configSrc <- IO(ConfigSource.default)
      apiConf = configSrc.at("pet.api").loadOrThrow[ApiConfig]
      dbConf = configSrc.at("pet.database").loadOrThrow[DbConfig]

      _ <- new FlywayMigrator().migrate(dbConf.url, dbConf.user, dbConf.pass)

      petRepo = getRepo(dbConf)
      server <- getServer(apiConf, petRepo)
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

  def getRepo(dbConf: DbConfig): PetRepo[IO] =
    new PetRepo[IO](
      Transactor.fromDriverManager[IO](dbConf.driver, dbConf.url, dbConf.user, dbConf.pass),
      new DoobieContext.Postgres(Literal),
    )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def getServer(apiConf: ApiConfig, petRepo: PetRepo[IO]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(apiConf.port, apiConf.host)
      .withHttpApp(
        List(
          new PetRoutes[IO](petRepo).routes,
          new SwaggerHttp4s(PetApi.endpoints.toOpenAPI("Pet Api", "0.1.0").toYaml).routes,
        ).foldK.orNotFound
      )
      .resource
      .use(_ => IO(StdIn.readLine()))
      .as(ExitCode.Success)

}
