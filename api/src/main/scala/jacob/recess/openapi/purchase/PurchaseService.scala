package jacob.recess.openapi

// import jacob.recess.openapi.app._
// import jacob.recess.openapi.database._
// import doobie.util.transactor.Transactor
// import io.getquill.Literal
// import doobie.quill._
// import jacob.recess.openapi.pet._
// import jacob.recess.openapi.customer._
// import jacob.recess.openapi.purchase._
// import cats.effect._
// import eu.timepit.refined.auto._
// import org.http4s.implicits._
// import org.http4s.server.Router
// import org.http4s.server.blaze._
// import pureconfig._
// import scala.io.StdIn
// import scala.concurrent.ExecutionContext.global

// object PurchaseStore extends IOApp {

//   @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
//   def run(args: List[String]): IO[ExitCode] = {
//     val server = for {
//       configSrc <- IO(ConfigSource.default)
//       apiConf = configSrc.at("api").loadOrThrow[ApiConfig]
//       dbConf = configSrc.at("database").loadOrThrow[DbConfig]

//       _ <- new FlywayMigrator().migrate(dbConf.url, dbConf.user, dbConf.pass)

//       tx = Transactor.fromDriverManager[IO](dbConf.driver, dbConf.url, dbConf.user, dbConf.pass)
//       doobieContext = new DoobieContext.Postgres(Literal)
//       petRepo = new PetRepo[IO](tx, doobieContext)
//       customerRepo = new CustomerRepo[IO](tx, doobieContext)
//       purchaseRepo = new PurchaseRepo[IO](tx, doobieContext)

//       petRoutes = new PetRoutes[IO](petRepo).routes
//       customerRoutes = new CustomerRoutes[IO, Customer](customerRepo).routes
//       purchaseRoutes = new PurchaseRoutes[IO, Purchase](purchaseRepo).routes
//       app = Router(
//         "/pet" -> petRoutes,
//         "/purchase" -> purchaseRoutes,
//         "/customer" -> customerRoutes,
//       ).orNotFound
//       server = BlazeServerBuilder[IO](global).bindHttp(apiConf.port, apiConf.host).withHttpApp(app)
//       fiber = server.resource.use(_ => IO(StdIn.readLine())).as(ExitCode.Success)
//     } yield fiber
//     server.attempt.unsafeRunSync() match {
//       case Left(e) =>
//         IO {
//           println("***** Something went wrong *****")
//           Option(e) match {
//             case Some(m) => println(m.getMessage)
//             case None => println("No error message")
//           }
//           ExitCode.Error
//         }
//       case Right(r) => r
//     }
//   }

// }
