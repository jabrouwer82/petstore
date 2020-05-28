package jacob.recess.openapi.pet

import jacob.recess.openapi.database._
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl._
import sttp.model._
import sttp.tapir.server.http4s._

class PetRoutes[F[_]: Sync: ContextShift: Http4sServerOptions](repo: Repo[F, Pet])
  extends Http4sDsl[F] {

  val getPet: HttpRoutes[F] =
    PetApi.getPet.toRoutes(uuid => repo.get(uuid).map(p => p.toRight(StatusCode.NotFound)))

  val getAllPets: HttpRoutes[F] = PetApi.getAllPets.toRoutes { _ =>
    repo.getAll().map(ps => Either.cond(ps.nonEmpty, ps, StatusCode.InternalServerError))
  }

  val updatePet: HttpRoutes[F] = PetApi.updatePet.toRoutes { pet =>
    repo.update(pet).map(t => Either.cond(t =!= 0, (), StatusCode.NotFound))
  }

  val createPet: HttpRoutes[F] = PetApi.createPet.toRoutes { pet =>
    repo.create(pet).map(t => Either.cond(t =!= 0, (), StatusCode.InternalServerError))
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val routes: HttpRoutes[F] =
    List(
      getPet,
      getAllPets,
      updatePet,
      createPet,
    ).foldK

}
