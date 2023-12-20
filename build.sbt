ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "akka-models"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.9.0-M2",
  "com.typesafe.akka" %% "akka-testkit" % "2.9.0-M2" % Test,
  "org.scalatest" %% "scalatest" % "3.3.0-SNAP4" % Test
)