package jacob.recess.openapi.purchase

import io.circe._
import io.circe.generic.semiauto._
import java.time.LocalDateTime
import java.util.UUID

final case class Purchase(
  uuid: UUID,
  pet: UUID,
  quantity: Int,
  shipDate: LocalDateTime,
  status: String,
  complete: Boolean,
)

object Purchase {
  implicit val encoder: Encoder[Purchase] = deriveEncoder[Purchase]
  implicit val decoder: Decoder[Purchase] = deriveDecoder[Purchase]
}
