package jacob.recess.openapi.api

import eu.timepit.refined.auto._
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.model._
import jacob.recess.openapi.pet._
import java.util.UUID

object PetApi {

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getPet: Endpoint[UUID, StatusCode, Pet, Nothing] = endpoint
    .get
    .in(
      "pet" / path[UUID]("uuid")
        .description("The UUID of a pet.")
        .example(ExamplePetData.examplePet.uuid)
    )
    .errorOut(statusCode)
    .out(
      jsonBody[Pet]
        .description("The pet associated with the given UUID.")
        .example(ExamplePetData.examplePet)
    )
    .description(
      """Returns the pet specified by the UUID given in the URL path. If the Pet does not exist, then an HTTP
        | 404 is returned""".stripMargin.replaceAll("\n", "")
    )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val getAllPets: Endpoint[Unit, StatusCode, List[Pet], Nothing] =
    endpoint
      .get
      .in("pet")
      .errorOut(statusCode)
      .out(
        jsonBody[List[Pet]]
          .description("All pets on the service.")
          .example(ExamplePetData.examplePets)
      )
      .description(
        """Returns the pet specified by the UUID given in the URL path. If the Pet does not exist, then an HTTP
          |404 is returned""".stripMargin.replaceAll("\n", " ")
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val updatePet: Endpoint[Pet, StatusCode, Unit, Nothing] =
    endpoint
      .put
      .in("pet")
      .in(
        jsonBody[Pet]
          .description("The Pet data which should be saved.")
          .example(ExamplePetData.examplePet)
      )
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "There is no Pet at the given UUID to update.")
          .description(StatusCode.BadRequest, "The given Pet json is malformed.")
          .description(StatusCode.InternalServerError, "Something unknown went wrong.")
      )
      .out(
        statusCode(StatusCode.NoContent)
          .description("No content is returned on successful update.")
      )
      .description(
        """Saves the given Pet to the database, updating data already at that Pet's uuid.
          |Returns a 404 if there is not already data at the given UUID."""
          .stripMargin
          .replaceAll("\n", " ")
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  val createPet: Endpoint[Pet, StatusCode, Unit, Nothing] =
    endpoint
      .post
      .in("pet")
      .in(
        jsonBody[Pet]
          .description("The Pet data that should be created.")
          .example(ExamplePetData.examplePet)
      )
      .errorOut(
        statusCode
          .description(StatusCode.NotFound, "There is no Pet at the given UUID to update.")
          .description(StatusCode.BadRequest, "The given Pet json is malformed.")
          .description(StatusCode.InternalServerError, "Something unknown went wrong.")
      )
      .out(
        statusCode(StatusCode.NoContent)
          .description("No content is returned on successful creation.")
      )
      .description(
        """Saves the given Pet to the database, only if there isn't already a Pet with the same UUID."""
      )

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.PublicInference"))
  val endpoints = List(
    getPet,
    getAllPets,
    updatePet,
    createPet,
  )

}

object ExamplePetData {

  val examplePet: Pet = Pet(
    UUID.fromString("d96644cc-c4fa-41c1-b308-cea1f715215f"),
    "Sam",
    "Lizard",
    "Yes",
  )

  val examplePets: List[Pet] = List(
    Pet(
      UUID.fromString("76a53801-2c0d-48f0-b4a9-e23f8ec7c2a2"),
      "Sally",
      "dog",
      "No",
    ),
    examplePet,
  )

}
