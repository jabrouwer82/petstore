package jacob.recess.openapi.customer

import eu.timepit.refined.auto._
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.model._
import jacob.recess.openapi.customer._
import java.util.UUID

object CustomerApi {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getCustomer: Endpoint[UUID, StatusCode, Customer, Nothing] = endpoint
    .get
    .tag("customer")
    .in(
      "customer" / path[UUID]("uuid")
        .description("The UUID of a customer.")
        .example(ExampleCustomerData.exampleCustomer.uuid)
    )
    .errorOut(statusCode)
    .out(
      jsonBody[Customer]
        .description("The customer associated with the given UUID.")
        .example(ExampleCustomerData.exampleCustomer)
    )
    .description(
      """Returns the customer specified by the UUID given in the URL path. If the Customer does not exist, then an HTTP
        | 404 is returned""".stripMargin.replaceAll("\n", "")
    )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getAllCustomers: Endpoint[Unit, StatusCode, List[Customer], Nothing] =
    endpoint
      .get
      .tag("customer")
      .in("customer")
      .errorOut(statusCode)
      .out(
        jsonBody[List[Customer]]
          .description("All customers on the service.")
          .example(ExampleCustomerData.exampleCustomers)
      )
      .description(
        """Returns the customer specified by the UUID given in the URL path. If the Customer does not exist, then an HTTP
          |404 is returned""".stripMargin.replaceAll("\n", " ")
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val updateCustomer: Endpoint[Customer, StatusCode, Unit, Nothing] =
    endpoint
      .put
      .tag("customer")
      .in("customer")
      .in(
        jsonBody[Customer]
          .description("The Customer data which should be saved.")
          .example(ExampleCustomerData.exampleCustomer)
      )
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "There is no Customer at the given UUID to update.")
          .description(StatusCode.BadRequest, "The given Customer json is malformed.")
          .description(StatusCode.InternalServerError, "Something unknown went wrong.")
      )
      .out(
        statusCode(StatusCode.NoContent)
          .description("No content is returned on successful update.")
      )
      .description(
        """Saves the given Customer to the database, updating data already at that Customer's uuid.
          |Returns a 404 if there is not already data at the given UUID."""
          .stripMargin
          .replaceAll("\n", " ")
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val createCustomer: Endpoint[Customer, StatusCode, Unit, Nothing] =
    endpoint
      .post
      .tag("customer")
      .in("customer")
      .in(
        jsonBody[Customer]
          .description("The Customer data that should be created.")
          .example(ExampleCustomerData.exampleCustomer)
      )
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "There is no Customer at the given UUID to update.")
          .description(StatusCode.BadRequest, "The given Customer json is malformed.")
          .description(StatusCode.InternalServerError, "Something unknown went wrong.")
      )
      .out(
        statusCode(StatusCode.NoContent)
          .description("No content is returned on successful creation.")
      )
      .description(
        """Saves the given Customer to the database, only if there isn't already a Customer with the same UUID."""
      )

  @SuppressWarnings(
    Array(
      "org.wartremover.warts.Any",
      "org.wartremover.warts.Nothing",
      "org.wartremover.warts.PublicInference",
    )
  )
  val endpoints = List(
    getCustomer,
    getAllCustomers,
    updateCustomer,
    createCustomer,
  )

}

object ExampleCustomerData {

  val exampleCustomer: Customer = Customer(
    UUID.fromString("938cffd7-c66e-4326-9a49-01c208cbab20"),
    "brussel",
    "Bruce",
    "Russell",
    "brussell@email.com",
    "hunter2",
    "555-555-5555",
  )

  val exampleCustomer2: Customer = Customer(
      UUID.fromString("fbddbc6b-ad52-440d-9265-ae854240a5f9"),
      "dsummers",
      "Daisy",
      "Summers",
      "dsummers@email.com",
      "hunter2",
      "555-555-5555",
    )

  val exampleCustomers: List[Customer] = List(
    exampleCustomer,
    exampleCustomer2,
  )

}
