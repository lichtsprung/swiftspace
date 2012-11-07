package net.swiftspace.core.structure

import akka.actor.{ActorRef, ActorLogging, Props, Actor}
import net.swiftspace.core.Simulation
import net.swiftspace.core.structure.Structure.{ReceiveResource, NewProcessingModule, NewStructure}
import net.swiftspace.core.Simulation.Coordinate
import net.swiftspace.core.structure.StructureManager.StructureInVicinity
import akka.util.Timeout
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.duration._


/**
 * Companion object that holds some actor messages used with the StructureManager.
 */
object StructureManager {

  /**
   * Used to ask the StructureManager what structures are in the vicinity of a coordinate.
   * @param coordinate the coordinate
   * @param distance the distance to that coordinate
   */
  case class StructureInVicinity(coordinate: Coordinate, distance: Double)

}

/**
 * Manages the structures inside the simulation
 */
class StructureManager extends Actor with ActorLogging {

  import net.swiftspace.core.Simulation.Tick


  def receive = {
    // Global update tick. Will be forwarded to all children.
    case Tick =>
      context.children.foreach(c => c ! Tick)

    // Add a new structure to the system. The descriptor parameter holds all necessary information about the new
    //structure.
    case NewStructure(descriptor) =>
      log.info("Spawning new Structure: " + descriptor.coordinate)
      val structure = context.actorOf(Props(new Structure(descriptor.coordinate)))

      descriptor.resources.foreach(resource => structure ! ReceiveResource(Simulation.configuration.resources(resource._1), resource._2))
      descriptor.processingUnits.foreach(unitDescriptor => structure ! NewProcessingModule(unitDescriptor))

    // The manager can answer the question what structures are in the vicinity to a coordinate by asking all children
    // whether they are within the vicinity circle.
    // After a timeout the list with all neighbours will be passed to the requesting actor in a future.
    case StructureInVicinity(coordinate, distance) =>
      import akka.dispatch.Future

      // Timeout for the future.
      implicit val timeout = Timeout(1.seconds)

      // The execution environment used by the sequence function.
      implicit val ec = context.dispatcher

      // All children actors are asked whether they are in the vicinity of this coordinate or not.
      val listOfFutures = context.children.map(child => (child ? StructureInVicinity(coordinate, distance)).mapTo[ActorRef])

      // Making it a Future of a list.
      val futureList = Future.sequence(listOfFutures)

      // Piping the Future to the requesting actor.
      futureList pipeTo sender


  }

}
