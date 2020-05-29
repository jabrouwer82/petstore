package jacob.recess.openapi.purchase

import cats.implicits._
import cats.data._
import jacob.recess.openapi.database._
import jacob.recess.openapi.pet._
import sttp.model._
import java.util.UUID
import sttp.tapir.client.sttp._
import sttp.tapir.DecodeResult._
import sttp.client.asynchttpclient.cats.AsyncHttpClientCatsBackend
import cats.effect._

class PurchaseHandler[F[_]: Concurrent: ContextShift](
  repo: Repo[F, Purchase],
  petUri: Uri,
) {

  val getPurchase: Http4sLogic[F, UUID, Purchase] = uuid =>
    repo.get(uuid).map(p => p.toRight(StatusCode.NotFound))

  val getAllPurchases: Http4sLogic[F, Unit, List[Purchase]] = _ =>
    repo.getAll().map(ps => Either.cond(ps.nonEmpty, ps, StatusCode.InternalServerError))

  val updatePurchase: Http4sLogic[F, Purchase, Unit] = purchase =>
    repo.update(purchase).map(t => Either.cond(t =!= 0, (), StatusCode.NotFound))

  val createPurchase: Http4sLogic[F, Purchase, Unit] = purchase =>
    repo.create(purchase).map(t => Either.cond(t =!= 0, (), StatusCode.InternalServerError))

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getPetForPurchase: Http4sLogic[F, UUID, Pet] = uuid =>
    OptionT(repo.get(uuid))
      .toRight(StatusCode.NotFound)
      .flatMapF(purchase =>
        AsyncHttpClientCatsBackend[F]().flatMap { implicit backend =>
          PetApi
            .getPet
            .toSttpRequest(petUri)
            .apply(purchase.pet)
            .send()
            .map(_.body match {
              case _: Failure => Left(StatusCode.InternalServerError)
              case Value(v) => v
            })
        }
      )
      .value

}
