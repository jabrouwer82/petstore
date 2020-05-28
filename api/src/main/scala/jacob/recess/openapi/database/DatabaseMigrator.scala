package jacob.recess.openapi.database

trait DatabaseMigrator[F[_]] {

  def migrate(
    url: String,
    user: String,
    pass: String,
  ): F[Int]

}
