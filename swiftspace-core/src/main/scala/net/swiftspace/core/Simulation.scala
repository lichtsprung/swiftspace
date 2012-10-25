package net.swiftspace.core

import akka.actor.{Props, ActorSystem, Actor}
import java.util.Date
import person.PersonManager
import structure.StructureManager


/**
 * A 3d coordinate of a point in space.
 *
 * @param x the x coordinate
 * @param y the y coordinate
 * @param z the z coordinate
 */
case class Coordinate(val x: Double, val y: Double, val z: Double) {
  /**
   * The euclidean distance between two points.
   * @param coord the other point
   * @return the distance between the points
   */
  def distance(coord: Coordinate) = math.sqrt(math.pow(x - coord.x, 2) + math.pow(y - coord.y, 2) + math.pow(z - coord.z, 2))
}

/**
 * A cubic area in space.
 *
 * @param width the width of the cube
 * @param height the height of the cube
 * @param depth the depth of the cube
 */
case class Dimension(val width: Double, val height: Double, val depth: Double) {
  /**
   * The volume of the cube.
   */
  val volume = width * height * depth
}

/**
 * Directions used in the simulation.
 */
object Direction extends Enumeration {
  type Direction = Value
  val Left, Right, Up, Down, Bottom, Top = Value
}


/**
 * The Simulation Actor.
 */
class Simulation extends Actor {
  val personManager = context.actorOf(Props[PersonManager], "PersonManager")
  val structureManager = context.actorOf(Props[StructureManager], "StructureManager")

  def receive = {
    case Tick(now) =>
      context.children.foreach(c => c ! Tick(now))
  }
}

/**
 * simulation clock that sends out a simulation tick once a second.
 */
class SimulationClock extends Actor {
  val simulation = context.actorFor("../simulation")
  var running = false
  var counter = 5
  var interval = 1

  val clock = new Thread(new Runnable {
    def run() {
      while (running) {
        println("Sending tick to " + simulation)
        simulation ! Tick(new Date())
        counter -= 1
        if (counter < 0) {
          running = false
          context.system.shutdown()
        }
        Thread.sleep(interval)
      }
    }
  })


  def receive = {
    case StartClock(interval) =>
      println("starting clock")
      this.interval = interval
      running = true
      clock.start()
    case StopClock() =>
      running = false
      context.system.shutdown()
    case ChangeTickInterval(interval) =>
      this.interval = interval
  }
}



