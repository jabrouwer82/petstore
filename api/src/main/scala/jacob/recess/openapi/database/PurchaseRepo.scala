package jacob.recess.openapi.database

import cats.implicits._
import cats.effect.Sync
import io.getquill.{idiom => _, _}
import doobie.quill._
import doobie.implicits._
import fs2.Stream
import jacob.recess.openapi.purchase._
import java.util.UUID
import doobie.util.transactor.Transactor

class PurchaseRepo[F[_]: Sync](tx: Transactor[F], ctx: DoobieContext.Postgres[Literal.type])
  extends Repo[F, Purchase] {
  import ctx._

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def get(uuid: UUID): F[Option[Purchase]] =
    run(
      quote {
        query[Purchase].filter(_.uuid == lift(uuid)).take(1)
      }
    ).transact(tx).map(_.headOption)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def getAll(): F[List[Purchase]] =
    run(
      quote {
        query[Purchase]
      }
    ).transact(tx)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def create(p: Purchase): F[Long] =
    run(
      quote {
        query[Purchase].insert(lift(p))
      }
    ).transact(tx)

  @SuppressWarnings(
    Array("org.wartremover.warts.JavaSerializable", "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Product", "org.wartremover.warts.Serializable")
  )
  override def update(p: Purchase): F[Long] =
    run(
      quote {
        query[Purchase].filter(_.uuid == lift(p.uuid)).update(lift(p))
      }
    ).transact(tx)

}
