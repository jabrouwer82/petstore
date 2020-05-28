package jacob.recess.openapi.database

import cats.effect.IO
import org.flywaydb.core.Flyway

final class FlywayMigrator extends DatabaseMigrator[IO] {

  override def migrate(
    url: String,
    user: String,
    pass: String,
  ): IO[Int] =
    IO(
      Flyway
        .configure()
        .dataSource(url, user, pass)
        .load()
        .migrate()
    )

}
