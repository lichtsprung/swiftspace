package net.swiftspace.core.structure

import akka.actor.{ActorRef, ActorLogging, Props, Actor}
import net.swiftspace.core.Simulation
import net.swiftspace.core.structure.Structure.{ReceiveResource, NewProcessingModule, NewStructure}
import net.swiftspace.core.Simulation.Coordinate
import net.swiftspace.core.structure.StructureManager.StructureInVicinty
import akka.util.Timeout
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.duration._



object StructureManager {

  case class StructureInVicinty(coordinate: Coordinate, distance: Double)

  case class InVicinity(actors: List[ActorRef])

}

/**
 * Manages the structures inside the simulation
 */
class StructureManager extends Actor with ActorLogging {

  import net.swiftspace.core.Simulation.Tick


  def receive = {
    case Tick =>
      context.children.foreach(c => c ! Tick)
    case NewStructure(descriptor) =>
      log.info("Spawning new Structure: " + descriptor.coordinate)
      val structure = context.actorOf(Props(new Structure(descriptor.coordinate)))

      descriptor.resources.foreach(resource => structure ! ReceiveResource(Simulation.configuration.resources(resource._1), resource._2))
      descriptor.processingUnits.foreach(unitDescriptor => structure ! NewProcessingModule(unitDescriptor))
    case StructureInVicinty(coordinate, distance) =>
      import akka.dispatch.{ExecutionContext, Future}
      log.info("asking structures...")
      implicit val timeout = Timeout(1.seconds)
      implicit val ec = context.dispatcher

      val listOfFutures = context.children.map(child => (child ? StructureInVicinty(coordinate, distance)).mapTo[ActorRef])
      val futureList = Future.sequence(listOfFutures)

      futureList pipeTo sender


  }

}
