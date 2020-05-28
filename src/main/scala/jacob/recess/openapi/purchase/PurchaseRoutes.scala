package jacob.recess.openapi.purchase

import cats.effect._
import jacob.recess.openapi.purchase._
import java.util.UUID
import org.http4s._
import sttp.model._
import sttp.tapir.server._
import sttp.tapir.server.http4s._

class PurchaseRoutes[F[_]: Sync: ContextShift: Http4sServerOptions](handler: PurchaseHandler[F]) {

  val getPurchase: ServerEndpoint[UUID, StatusCode, Purchase, Nothing, F] =
    PurchaseApi.getPurchase.serverLogic(handler.getPurchase)

  val getAllPurchases: ServerEndpoint[Unit, StatusCode, List[Purchase], Nothing, F] =
    PurchaseApi.getAllPurchases.serverLogic(handler.getAllPurchases)

  val updatePurchase: ServerEndpoint[Purchase, StatusCode, Unit, Nothing, F] =
    PurchaseApi.updatePurchase.serverLogic(handler.updatePurchase)

  val createPurchase: ServerEndpoint[Purchase, StatusCode, Unit, Nothing, F] =
    PurchaseApi.createPurchase.serverLogic(handler.createPurchase)

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.Any",
      "org.wartremover.warts.Nothing",
    )
  )
  val routes: HttpRoutes[F] = List(
    getPurchase,
    getAllPurchases,
    updatePurchase,
    createPurchase,
  ).toRoutes

}
