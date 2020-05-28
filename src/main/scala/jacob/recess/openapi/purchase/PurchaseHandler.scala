package jacob.recess.openapi.purchase

import cats._
import cats.implicits._
import jacob.recess.openapi.database._
import sttp.model._
import java.util.UUID

class PurchaseHandler[F[_]: Functor](repo: Repo[F, Purchase]) {

  val getPurchase: Http4sLogic[F, UUID, Purchase] = uuid =>
    repo.get(uuid).map(p => p.toRight(StatusCode.NotFound))

  val getAllPurchases: Http4sLogic[F, Unit, List[Purchase]] = _ =>
    repo.getAll().map(ps => Either.cond(ps.nonEmpty, ps, StatusCode.InternalServerError))

  val updatePurchase: Http4sLogic[F, Purchase, Unit] = purchase =>
    repo.update(purchase).map(t => Either.cond(t =!= 0, (), StatusCode.NotFound))

  val createPurchase: Http4sLogic[F, Purchase, Unit] = purchase =>
    repo.create(purchase).map(t => Either.cond(t =!= 0, (), StatusCode.InternalServerError))

}
