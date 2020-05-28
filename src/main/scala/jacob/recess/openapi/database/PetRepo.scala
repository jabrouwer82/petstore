package jacob.recess.openapi.database

import cats.implicits._
import cats.effect.Sync
import io.getquill.{idiom => _, _}
import doobie.quill._
import doobie.implicits._
import jacob.recess.openapi.pet._
import java.util.UUID
import doobie.util.transactor.Transactor

class PetRepo[F[_]: Sync](tx: Transactor[F], ctx: DoobieContext.Postgres[Literal.type])
  extends Repo[F, Pet] {
  import ctx._

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def get(uuid: UUID): F[Option[Pet]] =
    run(
      quote {
        query[Pet].filter(_.uuid == lift(uuid)).take(1)
      }
    ).transact(tx).map(_.headOption)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def getAll(): F[List[Pet]] =
    run(
      quote {
        query[Pet]
      }
    ).transact(tx)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def create(p: Pet): F[Long] =
    run(
      quote {
        query[Pet].insert(lift(p))
      }
    ).transact(tx)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def update(p: Pet): F[Long] =
    run(
      quote {
        query[Pet].filter(_.uuid == lift(p.uuid)).update(lift(p))
      }
    ).transact(tx)

}
