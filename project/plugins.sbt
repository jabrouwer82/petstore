addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

addCompilerPlugin("io.tryp" % "splain" % "0.5.6" cross CrossVersion.patch)

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.1")
addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.7")
addSbtPlugin("com.github.xuwei-k" % "sbt-class-diagram" % "0.2.1")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "6.4.2")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "2.4.7")
addSbtPlugin("org.wartremover" % "sbt-wartremover-contrib" % "1.3.5")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.1.0")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.15")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.0")
addSbtPlugin("com.alejandrohdezma" %% "sbt-fix" % "0.5.0")
addSbtPlugin("com.alejandrohdezma" %% "sbt-scalafix-defaults" % "0.2.2")
addSbtPlugin("com.alejandrohdezma" %% "sbt-scalafmt-defaults" % "0.1.1")