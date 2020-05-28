package jacob.recess.openapi.purchase

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

object PurchaseStore extends IOApp {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def run(args: List[String]): IO[ExitCode] = {
    val server = for {
      configSrc <- IO(ConfigSource.default)
      apiConf = configSrc.at("purchase.api").loadOrThrow[ApiConfig]
      dbConf = configSrc.at("purchase.database").loadOrThrow[DbConfig]

      _ <- new FlywayMigrator().migrate(dbConf.url, dbConf.user, dbConf.pass)

      purchaseRepo = getHandler(getRepo(dbConf))
      server <- getServer(apiConf, purchaseRepo)
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

  def getRepo(dbConf: DbConfig): PurchaseRepo[IO] =
    new PurchaseRepo[IO](
      Transactor.fromDriverManager[IO](dbConf.driver, dbConf.url, dbConf.user, dbConf.pass),
      new DoobieContext.Postgres(Literal),
    )

  def getHandler(repo: PurchaseRepo[IO]): PurchaseHandler[IO] =
    new PurchaseHandler(repo)

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def getServer(apiConf: ApiConfig, handler: PurchaseHandler[IO]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(apiConf.port, apiConf.host)
      .withHttpApp(
        List(
          new PurchaseRoutes[IO](handler).routes,
          new SwaggerHttp4s(PurchaseApi.endpoints.toOpenAPI("Purchase Api", "0.1.0").toYaml).routes,
        ).foldK.orNotFound
      )
      .resource
      .use(_ => IO(StdIn.readLine()))
      .as(ExitCode.Success)

}
