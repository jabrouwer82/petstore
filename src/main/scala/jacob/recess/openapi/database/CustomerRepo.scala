package jacob.recess.openapi.database

import cats.implicits._
import cats.effect.Sync
import io.getquill.{idiom => _, _}
import doobie.quill._
import doobie.implicits._
import jacob.recess.openapi.customer._
import java.util.UUID
import doobie.util.transactor.Transactor

class CustomerRepo[F[_]: Sync](tx: Transactor[F], ctx: DoobieContext.Postgres[Literal.type])
  extends Repo[F, Customer] {
  import ctx._

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def get(uuid: UUID): F[Option[Customer]] =
    run(
      quote {
        query[Customer].filter(_.uuid == lift(uuid)).take(1)
      }
    ).transact(tx).map(_.headOption)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def getAll(): F[List[Customer]] =
    run(
      quote {
        query[Customer]
      }
    ).transact(tx)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def create(p: Customer): F[Long] =
    run(
      quote {
        query[Customer].insert(lift(p))
      }
    ).transact(tx)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def update(p: Customer): F[Long] =
    run(
      quote {
        query[Customer].filter(_.uuid == lift(p.uuid)).update(lift(p))
      }
    ).transact(tx)

}
