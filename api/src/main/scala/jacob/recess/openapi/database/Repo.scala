package jacob.recess.openapi.database

import fs2.Stream
import java.util.UUID

trait Repo[F[_], T] {
  def get(uuid: UUID): F[Option[T]]
  def getAll(): F[List[T]]
  def create(p: T): F[Long]
  def update(p: T): F[Long]
}
