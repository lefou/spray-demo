name := "spray-demo"

version := "1.0"

scalaVersion := "2.10.4"

val sprayVersion = "1.2.1"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.4",
  "io.spray" % "spray-routing" % sprayVersion,
  "io.spray" % "spray-can" % sprayVersion,
  "io.spray" % "spray-client" % sprayVersion,
  "org.json4s" %% "json4s-native" % "3.2.9",
  "org.scalatest" %% "scalatest" % "2.1.7" % "test",
  "io.spray" % "spray-testkit" % sprayVersion % "test"
)

EclipseKeys.withSource := true
