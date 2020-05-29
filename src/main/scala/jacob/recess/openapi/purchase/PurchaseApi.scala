package jacob.recess.openapi.purchase

import eu.timepit.refined.auto._
import jacob.recess.openapi.purchase._
import jacob.recess.openapi.pet._
import java.time.LocalDateTime
import java.util.UUID
import sttp.model._
import sttp.tapir._
import sttp.tapir.json.circe._
import jacob.recess.openapi.customer._

object PurchaseApi {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getPurchase: Endpoint[UUID, StatusCode, Purchase, Nothing] = endpoint
    .get
    .tag("purchase")
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
      .tag("purchase")
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
      .tag("purchase")
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
      .tag("purchase")
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

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getPetForPurchase: Endpoint[UUID, StatusCode, Pet, Nothing] =
    endpoint
      .get
      .tag("purchase")
      .in("purchase/pet" / path[UUID]("uuid"))
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "There is no Purchase at the given UUID.")
          .description(StatusCode.InternalServerError, "Something unknown went wrong.")
      )
      .out(
        jsonBody[Pet]
          .description("The pet associated with the given purchase uuid.")
          .example(ExamplePetData.examplePet)
      )
      .description(
        """Fetches the given purchase, then fetches the associated pet from the Pet Service."""
      )

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.Any",
      "org.wartremover.warts.Nothing",
      "org.wartremover.warts.PublicInference",
    )
  )
  val endpoints = List(
    getPurchase,
    getAllPurchases,
    updatePurchase,
    createPurchase,
    getPetForPurchase,
  )

}

object ExamplePurchaseData {

  val examplePurchase: Purchase = Purchase(
    ExampleCustomerData.exampleCustomer.uuid,
    ExamplePetData.examplePet.uuid,
    1,
    LocalDateTime.of(2020, 5, 17, 11, 55, 29),
    "Completed",
    true,
  )

  val examplePurchase2: Purchase = Purchase(
      ExampleCustomerData.exampleCustomer2.uuid,
      ExamplePetData.examplePet2.uuid,
      1,
      LocalDateTime.of(2020, 5, 18, 14, 33, 12),
      "Denied",
      true,
    )

  val examplePurchases: List[Purchase] = List(
    examplePurchase,
    examplePurchase2,
  )

}
