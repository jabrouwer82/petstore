package jacob.recess.openapi.customer

import cats.effect._
import cats.implicits._
import jacob.recess.openapi.database._
import sttp.model._
import sttp.tapir.server.akkahttp._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._

class CustomerRoutes(repo: Repo[IO, Customer]) {

  val getCustomer: Route = CustomerApi.getCustomer.toRoute { uuid =>
    repo
      .get(uuid)
      .map(p => p.toRight(StatusCode.NotFound))
      .unsafeToFuture()
  }

  val getAllCustomers: Route = CustomerApi.getAllCustomers.toRoute { _ =>
    repo
      .getAll()
      .map(ps => Either.cond(ps.nonEmpty, ps, StatusCode.InternalServerError))
      .unsafeToFuture()
  }

  val updateCustomer: Route = CustomerApi.updateCustomer.toRoute { customer =>
    repo
      .update(customer)
      .map(t => Either.cond(t =!= 0, (), StatusCode.NotFound))
      .unsafeToFuture()
  }

  val createCustomer: Route = CustomerApi.createCustomer.toRoute { customer =>
    repo
      .create(customer)
      .map(t => Either.cond(t =!= 0, (), StatusCode.InternalServerError))
      .unsafeToFuture()
  }

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.Any",
      "org.wartremover.warts.Nothing",
      "org.wartremover.warts.TraversableOps",
    )
  )
  val routes: Route =
    List(
      getCustomer,
      getAllCustomers,
      updateCustomer,
      createCustomer,
    ).reduceLeft((acc, cur) => acc ~ cur)

}
