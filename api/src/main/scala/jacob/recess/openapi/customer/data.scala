package jacob.recess.openapi.customer

import java.util.UUID
import io.circe._
import io.circe.generic.semiauto._

final case class Customer(
  uuid: UUID,
  username: String,
  firstName: String,
  lastName: String,
  email: String,
  password: String,
  phone: String,
)

object Customer {
  implicit val encoder: Encoder[Customer] = deriveEncoder[Customer]
  implicit val decoder: Decoder[Customer] = deriveDecoder[Customer]
}
