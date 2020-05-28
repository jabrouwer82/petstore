lazy val api = project
  .settings(
    name := "api"
  )

// lazy val pets = project
//   .settings(
//     name := "pets",
//   )

ThisBuild / organization := "jacob.recess"
ThisBuild / version := "0.0.1-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.2"
ThisBuild / wartremoverWarnings ++= Warts.all
ThisBuild / scapegoatVersion := "1.4.4"

val CatsEffectVersion = "2.1.3"
val CatsTimeVersion = "0.3.0"
val CatsVersion = "2.1.1"
val CirceVersion = "0.13.0"
val DoobieVersion = "0.8.8"
val FlywayVersion = "6.4.2"
val Http4sVersion = "0.21.4"
val KittensVersion = "2.1.0"
val LogbackVersion = "1.2.3"
val MouseVersion = "0.25"
val PsqlVersion = "42.2.12"
val QuillVersion = "3.5.1"
val PureconfigVersion = "0.12.3"
val RefinedVersion = "0.9.14"
val Specs2Version = "4.9.3"
val TapirVersion = "0.15.0"

ThisBuild / libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % LogbackVersion,
  "com.github.pureconfig" %% "pureconfig" % PureconfigVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-core" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-cats" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-refined" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % TapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % TapirVersion,
  "eu.timepit" %% "refined" % RefinedVersion,
  "eu.timepit" %% "refined-cats" % RefinedVersion,
  "eu.timepit" %% "refined-pureconfig" % RefinedVersion,
  "io.chrisdavenport" %% "cats-time" % CatsTimeVersion,
  "io.circe" %% "circe-core" % CirceVersion,
  "io.circe" %% "circe-generic" % CirceVersion,
  "io.circe" %% "circe-parser" % CirceVersion,
  "io.circe" %% "circe-refined" % CirceVersion,
  "io.getquill" %% "quill-jdbc" % QuillVersion,
  "org.flywaydb" % "flyway-core" % FlywayVersion,
  "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "org.postgresql" % "postgresql" % PsqlVersion,
  "org.specs2" %% "specs2-core" % Specs2Version % "test",
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
  "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
  "org.tpolecat" %% "doobie-quill" % DoobieVersion,
  "org.tpolecat" %% "doobie-refined" % DoobieVersion,
  "org.typelevel" %% "cats-core" % CatsVersion,
  "org.typelevel" %% "cats-effect" % CatsEffectVersion,
  "org.typelevel" %% "kittens" % KittensVersion,
  "org.typelevel" %% "mouse" % MouseVersion,
)

ThisBuild / scalacOptions ++= Seq(
  // format: off
  "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
  // "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Ybackend-parallelism", "8", // Enable paralellisation — change to desired number!
  "-Ycache-macro-class-loader:last-modified", // and macro definitions. This can lead to performance improvements.
  "-Ycache-plugin-class-loader:last-modified", // Enables caching of classloaders for compiler plugins
  "-Yrangepos",
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding", "UTF-8",
  "-explaintypes", // Explain type errors in more detail.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros", // Allow macro definition (besides implementation and application)
  "-language:higherKinds", // Allow higher-kinded types
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-language:postfixOps",
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  // format: on
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
addCompilerPlugin(("io.tryp" % "splain" % "0.5.6").cross(CrossVersion.patch))
addCompilerPlugin(scalafixSemanticdb("4.3.10"))

ThisBuild / scalafixDependencies ++= Seq(
  "com.github.vovapolu" %% "scaluzzi" % "0.1.7"
)
