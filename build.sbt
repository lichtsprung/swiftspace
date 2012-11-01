version := "0.1"

scalaVersion := "2.9.2"

name := "SwiftSpace Core"

organization := "net.swiftspace.core"

fork in run := true

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.3"

libraryDependencies += "com.twitter"   % "util-eval"   % "5.3.13"

