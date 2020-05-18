package jacob.recess.openapi.data

import java.util.UUID
import java.time.OffsetDateTime

package object pet {
  final case class Category(
    uuid: UUID,
    name: String,
  )

  final case class Tag(
    uuid: UUID,
    name: String,
  )

  sealed trait Status
  object Available extends Status

  final case class Pet(
    uuid: UUID,
    category: Category,
    name: String,
    photoUrls: List[String],
    tags: List[Tag],
    status: Status,
  )
}

package object order {
  sealed trait Status
  object Placed extends Status

  final case class Order(
    uuid: UUID,
    pet: UUID,
    quantity: Int,
    shipDate: OffsetDateTime,
    status: Status,
    complete: Boolean,
  )

  type Inventory = Map[Status, Int]
}

package object user {
  sealed trait Status
  object Zero extends Status

  final case class User(
    uuid: UUID,
    username: String,
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    phone: String,
    status: Status,
  )
}
