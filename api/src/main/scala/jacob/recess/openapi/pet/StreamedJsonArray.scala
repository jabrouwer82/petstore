package jacob.recess.openapi.pet

import fs2.Stream
import io.circe._
import org.http4s._
import org.http4s.headers._

final case class StreamedJsonArray[F[_], E](stream: Stream[F, E])

object StreamedJsonArray {

  implicit def entityEncoder[F[_], E: Encoder]: EntityEncoder[F, StreamedJsonArray[F, E]] =
    new EntityEncoder[F, StreamedJsonArray[F, E]] {

      override def toEntity(a: StreamedJsonArray[F, E]): Entity[F] = {
        val stream = Stream
          .emit("[") ++ a.stream.map(Encoder[E].apply).map(_.noSpaces).intersperse(",") ++ Stream
          .emit("]")
        Entity(stream.through(fs2.text.utf8Encode[F]))
      }

      override def headers: Headers = Headers(`Content-Type`(MediaType.application.json))
    }

}
