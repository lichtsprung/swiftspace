package net.swiftspace.core.structure

import akka.actor.{ActorLogging, Props, Actor}
import net.swiftspace.core.structure.Structure.{NewProcessingModule, NewStructure}
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Module.WaterProcessor


object StructureManager {

}

/**
 * Manages the structures inside the simulation
 */
class StructureManager extends Actor with ActorLogging {

  import net.swiftspace.core.Simulation.Tick

  def receive = {
    case Tick =>
      context.children.foreach(c => c ! Tick)
    case NewStructure(coordinate) =>
      log.info("Spawning new Structure: " + coordinate)
      val ns = context.actorOf(Props(new Structure(coordinate)))
      ns ! NewProcessingModule(new WaterProcessor(2))
  }

}
