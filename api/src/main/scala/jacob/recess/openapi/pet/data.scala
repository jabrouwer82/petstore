package jacob.recess.openapi.pet

import io.circe._
import io.circe.generic.semiauto._
import java.util.UUID

final case class Pet(
  uuid: UUID,
  name: String,
  category: String,
  status: String,
)

object Pet {
  implicit val encoder: Encoder[Pet] = deriveEncoder[Pet]
  implicit val decoder: Decoder[Pet] = deriveDecoder[Pet]
}
