package net.swiftspace.core.structure

import akka.actor.{ActorRef, ActorLogging, Props, Actor}
import net.swiftspace.core.structure.Structure.{ReceiveResource, NewProcessingModule, NewStructure}
import xml.XML
import java.io.File
import com.twitter.util.Eval
import net.swiftspace.core.config.Config
import net.swiftspace.core.Simulation


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
    case NewStructure(descriptor) =>
      log.info("Spawning new Structure: " + descriptor.coordinate)
      val structure = context.actorOf(Props(new Structure(descriptor.coordinate)))
      descriptor.resources.foreach(resource => structure ! ReceiveResource(Simulation.configuration.resources(resource._1), resource._2))
      descriptor.processingUnits.foreach(unitDescriptor => structure ! NewProcessingModule(unitDescriptor))
  }

}
