version := "0.1"

scalaVersion := "2.10.1"

name := "SwiftSpace Core"

organization := "net.swiftspace.core"

fork in run := true

resolvers += "Typesafe Snapshot Repository" at "http://repo.typesafe.com/typesafe/snapshots/"

resolvers += "Typesafe Release Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Scala Tools Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.2-SNAPSHOT"

libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.10" % "2.2-SNAPSHOT"

libraryDependencies += "com.twitter" % "util-eval_2.10" % "6.3.0"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"


