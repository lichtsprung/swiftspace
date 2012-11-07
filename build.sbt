version := "0.1"

scalaVersion := "2.9.2"

name := "SwiftSpace Core"

organization := "net.swiftspace.core"

fork in run := true

resolvers += "Typesafe Snapshot Repository" at "http://repo.typesafe.com/typesafe/snapshots/"

resolvers += "Typesafe Release Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Scala Tools Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.3"

libraryDependencies += "com.twitter"   % "util-eval"   % "5.3.13"


