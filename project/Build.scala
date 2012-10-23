import sbt._
import Keys._

object SwiftspaceBuild extends Build {
    lazy val root = Project(id = "root",
                            base = file(".")) aggregate(core, ui)
                         
    lazy val core = Project(id = "swiftspace-core",
                           base = file("swiftspace-core"))

    lazy val ui = Project(id = "swiftspace-ui",
                           base = file("swiftspace-ui")) dependsOn(core)
   
}
