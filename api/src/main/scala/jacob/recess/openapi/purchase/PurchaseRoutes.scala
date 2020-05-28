package jacob.recess.openapi.purchase

import cats.effect._
import cats.implicits._
import jacob.recess.openapi.database._
import mouse.all._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._

class PurchaseRoutes[F[_]: Sync](repo: Repo[F, Purchase]) extends Http4sDsl[F] {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  implicit def tEncoder: EntityEncoder[F, Purchase] = jsonEncoderOf

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  implicit def tListEncoder: EntityEncoder[F, List[Purchase]] = jsonEncoderOf

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  implicit def tDencoder: EntityDecoder[F, Purchase] = jsonOf

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root / UUIDVar(uuid) =>
      Sync[F].delay(println(s"get ${req.uri.toString}")) *>
        repo.get(uuid).flatMap(ts => ts.cata(Ok(_), NotFound()))
    case req @ GET -> Root =>
      Sync[F].delay(println(s"get ${req.uri.toString}")) *>
      repo.getAll().flatMap(ps => if (ps.nonEmpty) Ok(ps) else NotFound())
    case req @ PUT -> Root =>
      Sync[F].delay(println(s"put ${req.uri.toString}")) *> {
        for {
          r <- req.as[Purchase]
          t <- repo.update(r)
          res <-
            if (t === 0) NotFound()
            else NoContent()
        } yield res
      }.handleErrorWith {
        case InvalidMessageBodyFailure(_, _) => BadRequest()
      }
    case req @ POST -> Root =>
      Sync[F].delay(println(s"post ${req.uri.toString}")) *> {
        for {
          _ <- Sync[F].delay(println(s"Running ${req.body.toString}"))
          r <- req.as[Purchase]
          _ <- Sync[F].delay(println(s"Converted ${r.toString}"))
          t <- repo.create(r)
          _ <- Sync[F].delay(println(s"Inserted ${t.toString}"))
          res <-
            if (t === 0) InternalServerError()
            else NoContent()
        } yield res
      }.handleErrorWith {
        case InvalidMessageBodyFailure(_, _) => BadRequest()
      }

  }

}
