name := "EmojiGenerator"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= List(
  "org.typelevel" %% "cats-core" % "2.5.0",
  "com.sksamuel.scrimage" % "scrimage-core" % "4.0.17"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _ @_*) => MergeStrategy.discard
  case _                           => MergeStrategy.first
}
