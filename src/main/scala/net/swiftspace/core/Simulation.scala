package net.swiftspace.core

import akka.actor.{Props, ActorLogging, Actor}
import com.twitter.util.Eval
import config.Config
import java.io.File
import net.swiftspace.core.Simulation._
import processing.Resource
import structure.Structure.NewStructure
import structure.StructureManager
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Companion object with utility functions.
 */
object Simulation {

  // Loading configuration file from the classpath.
  val url = getClass.getClassLoader.getResource("Configuration.scala").getFile

  // Compiling the configuration file.
  val configuration = Eval[Config](new File(url))

  // The current tick rate.
  var tickRate = 1.second

  // The update tick.
  case object Tick

  /**
   * Message to notify the simulation that the tick rate has been changed.
   * @param rate the new tick rate
   */
  case class TickRate(rate: FiniteDuration)

  /**
   * A 3d coordinate
   * @param x x axis
   * @param y y axis
   * @param z z axis
   */
  case class Coordinate(x: Double, y: Double, z: Double) {
    /**
     * Calculates the distance between two points.
     * @param c the other point
     * @return the euclidean distance
     */
    def distance(c: Coordinate): Double = math.sqrt(math.pow(x - c.x, 2) + math.pow(y - c.y, 2) + math.pow(z - c.z, 2))
  }

  /**
   * Returns the resource (if known) for a given name.
   * @param name the name of the resource
   * @return the resource
   */
  def getResource(name: String): Resource = {
    configuration.resources.getOrElse(name, Resource.None)
  }

}

/**
 * The simulation actor.
 */
class Simulation extends Actor with ActorLogging {

  // ActorRef to the StructureManager.
  val structure = context.actorOf(Props[StructureManager], "structuremanager")

  // Initialising the simulation with the loaded configuration.
  Simulation.configuration.structures.foreach(s => {
    log.info("Spawning new structure: " + s._2)
    structure ! NewStructure(s._2)
  })

  // Test: Ist eine Raumstruktur im Umkreis von acht Einheiten von der Koordinate (1,1,6)?
  //  implicit val timeout = Timeout(1.seconds)
  //  val list = structure ? StructureInVicinity(Coordinate(1, 1, 6), 8)
  //  println(list)
  //  list.onSuccess{case m => println("teeeeest: " + m)}

  // Default tick rate is one second
  var ticker = context.system.scheduler.schedule(1 second, 1 second, self, Tick)


  def receive = {
    //  Receives the update tick from the scheduler and forwards the tick to all children.
    case Tick =>
      context.children.foreach(child => child ! Tick)

    // Tick rate was changed.
    case TickRate(rate) =>
      ticker.cancel()
      Simulation.tickRate = rate
      ticker = context.system.scheduler.schedule(rate, rate, self, Tick)
  }

  override def postStop() {
    ticker.cancel()
  }
}





