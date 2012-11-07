version := "0.1"

scalaVersion := "2.10.0-RC1"

name := "SwiftSpace Core"

organization := "net.swiftspace.core"

fork in run := true

resolvers += "Typesafe Snapshot Repository" at "http://repo.typesafe.com/typesafe/snapshots/"

resolvers += "Typesafe Release Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Scala Tools Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10.0-RC1" % "2.1.0-RC1"

libraryDependencies += "com.twitter"   % "util-eval"   % "5.3.13"


