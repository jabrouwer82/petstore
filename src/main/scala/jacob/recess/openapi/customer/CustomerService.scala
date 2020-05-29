package jacob.recess.openapi.customer

import akka.actor._
import akka.http.scaladsl.Http._
import akka.http.scaladsl._
import akka.http.scaladsl.server.Directives._
import cats.effect._
import doobie.quill._
import doobie.util.transactor._
import eu.timepit.refined.auto._
import io.getquill._
import jacob.recess.openapi.app._
import jacob.recess.openapi.database._
import pureconfig._
import scala.io._
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.swagger.akkahttp._

object CustomerSerice extends IOApp {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def run(args: List[String]): IO[ExitCode] = {
    val server = for {
      configSrc <- Resource.liftF(IO(ConfigSource.default))
      apiConf = configSrc.at("customer.api").loadOrThrow[ApiConfig]
      dbConf = configSrc.at("customer.database").loadOrThrow[DbConfig]

      _ <- Resource.liftF(new FlywayMigrator().migrate(dbConf.url, dbConf.user, dbConf.pass))

      system <- actorSystem("my-system")
      customerRepo = getRepo(dbConf)
      server <- getServer(apiConf, customerRepo)(system)
    } yield server

    server
      .use(_ => IO(StdIn.readLine()))
      .as(ExitCode.Success)
      .attempt
      .unsafeRunSync() match {
      case Left(e) =>
        IO {
          println("***** Something went wrong *****")
          Option(e) match {
            case Some(m) => println(m.getMessage)
            case None => println("No error message")
          }
          ExitCode.Error
        }
      case Right(r) => IO(r)
    }
  }

  def getRepo(dbConf: DbConfig): CustomerRepo[IO] =
    new CustomerRepo[IO](
      Transactor.fromDriverManager[IO](dbConf.driver, dbConf.url, dbConf.user, dbConf.pass),
      new DoobieContext.Postgres(Literal),
    )

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.Any",
      "org.wartremover.warts.Nothing",
      "org.wartremover.warts.ImplicitParameter",
    )
  )
  def getServer(
    apiConf: ApiConfig,
    customerRepo: CustomerRepo[IO],
  )(
    implicit
    actor: ActorSystem
  ): Resource[IO, ServerBinding] =
    Resource.make(
      IO.fromFuture(
        IO(
          Http().bindAndHandle(
            new CustomerRoutes(customerRepo).routes ~ new SwaggerAkka(
              CustomerApi.endpoints.toOpenAPI("Customer Api", "0.1.0").toYaml
            ).routes,
            apiConf.host,
            apiConf.port,
          )
        )
      )
    )(sb =>
      IO.fromFuture(
          IO(sb.unbind())
        )
        .map(_ => println("Server unbound from port."))
    )

  def actorSystem(name: String): Resource[IO, ActorSystem] =
    Resource.make(IO(ActorSystem(name)))(as =>
      IO(as.terminate()).map(_ => println("Actor system terminated"))
    )

}
