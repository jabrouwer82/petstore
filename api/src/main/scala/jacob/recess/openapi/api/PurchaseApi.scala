package jacob.recess.openapi.api

import eu.timepit.refined.auto._
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.model._
import jacob.recess.openapi.purchase._
import java.util.UUID
import java.time.LocalDateTime

object PurchaseApi {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getPurchase: Endpoint[UUID, StatusCode, Purchase, Nothing] = endpoint
    .get
    .in(
      "purchase" / path[UUID]("uuid")
        .description("The UUID of a purchase.")
        .example(ExamplePurchaseData.examplePurchase.uuid)
    )
    .errorOut(statusCode)
    .out(
      jsonBody[Purchase]
        .description("The purchase associated with the given UUID.")
        .example(ExamplePurchaseData.examplePurchase)
    )
    .description(
      """Returns the purchase specified by the UUID given in the URL path. If the Purchase does not exist, then an HTTP
        | 404 is returned""".stripMargin.replaceAll("\n", "")
    )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getAllPurchases: Endpoint[Unit, StatusCode, List[Purchase], Nothing] =
    endpoint
      .get
      .in("purchase")
      .errorOut(statusCode)
      .out(
        jsonBody[List[Purchase]]
          .description("All purchases on the service.")
          .example(ExamplePurchaseData.examplePurchases)
      )
      .description(
        """Returns the purchase specified by the UUID given in the URL path. If the Purchase does not exist, then an HTTP
          |404 is returned""".stripMargin.replaceAll("\n", " ")
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val updatePurchase: Endpoint[Purchase, StatusCode, Unit, Nothing] =
    endpoint
      .put
      .in("purchase")
      .in(
        jsonBody[Purchase]
          .description("The Purchase data which should be saved.")
          .example(ExamplePurchaseData.examplePurchase)
      )
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "There is no Purchase at the given UUID to update.")
          .description(StatusCode.BadRequest, "The given Purchase json is malformed.")
          .description(StatusCode.InternalServerError, "Something unknown went wrong.")
      )
      .out(
        statusCode(StatusCode.NoContent)
          .description("No content is returned on successful update.")
      )
      .description(
        """Saves the given Purchase to the database, updating data already at that Purchase's uuid.
          |Returns a 404 if there is not already data at the given UUID."""
          .stripMargin
          .replaceAll("\n", " ")
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val createPurchase: Endpoint[Purchase, StatusCode, Unit, Nothing] =
    endpoint
      .post
      .in("purchase")
      .in(
        jsonBody[Purchase]
          .description("The Purchase data that should be created.")
          .example(ExamplePurchaseData.examplePurchase)
      )
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "There is no Purchase at the given UUID to update.")
          .description(StatusCode.BadRequest, "The given Purchase json is malformed.")
          .description(StatusCode.InternalServerError, "Something unknown went wrong.")
      )
      .out(
        statusCode(StatusCode.NoContent)
          .description("No content is returned on successful creation.")
      )
      .description(
        """Saves the given Purchase to the database, only if there isn't already a Purchase with the same UUID."""
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.PublicInference"))
  val endpoints = List(
    getPurchase,
    getAllPurchases,
    updatePurchase,
    createPurchase,
  )

}

object ExamplePurchaseData {

  val examplePurchase: Purchase = Purchase(
    UUID.fromString("dbd426ad-c755-48f3-88e7-6c27ad2f8dc2"),
    UUID.fromString("d96644cc-c4fa-41c1-b308-cea1f715215f"),
    1,
    LocalDateTime.of(2020, 5, 17, 11, 55, 29),
    "Completed",
    true,
  )

  val examplePurchases: List[Purchase] = List(
    Purchase(
      UUID.fromString("f29c14c9-f74d-46db-811e-0946a2e89bc5"),
      UUID.fromString("d96644cc-c4fa-41c1-b308-cea1f715215f"),
      1,
      LocalDateTime.of(2020, 5, 18, 14, 33, 12),
      "Denied",
      true,
    ),
    examplePurchase,
  )

}
