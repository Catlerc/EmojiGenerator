name := "EmojiGenerator"
version := "0.1"
organization in ThisBuild := "com.es"

lazy val commonDependencies = List(
  "com.sksamuel.scrimage" % "scrimage-core" % "4.0.17",
  "org.typelevel" %% "cats-effect" % "3.0.1",
  "org.typelevel" %% "cats-effect-kernel" % "3.0.0",
  "org.typelevel" %% "cats-core" % "2.5.0"
)
lazy val commonSettings = List(
  scalaVersion := "2.13.5"
)

lazy val global = project
  .in(file("."))
  .settings(commonSettings)
  .aggregate(
    common,
    userInterface,
    imageProcessing,
    app
  )

lazy val common = project
  .settings(
    commonSettings,
    name := "common",
    libraryDependencies ++= commonDependencies
  )

lazy val app = project
  .settings(
    commonSettings,
    name := "app",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    common,
    userInterface,
    imageProcessing
  )

lazy val userInterface = project
  .settings(
    commonSettings,
    name := "userInterface",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    common
  )

lazy val imageProcessing = project
  .settings(
    commonSettings,
    name := "imageProcessing",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(
    common
  )
