package net.swiftspace.core.structure

import akka.actor.{ActorLogging, Props, Actor}
import net.swiftspace.core.structure.Structure.{NewProcessingModule, NewStructure}
import net.swiftspace.core.structure.Module.{PowerGenerator, Electrolyser, WaterGenerator}
import xml.XML
import java.io.File


object StructureManager {

}

/**
 * Manages the structures inside the simulation
 */
class StructureManager extends Actor with ActorLogging {

  val modules = XML.loadFile(getClass.getClassLoader.getResource("modules.xml").getFile)
  val units = modules \ "processing" \ "unit"
  units.foreach(unit => {})

  import net.swiftspace.core.Simulation.Tick

  def receive = {
    case Tick =>
      context.children.foreach(c => c ! Tick)
    case NewStructure(coordinate) =>
      log.info("Spawning new Structure: " + coordinate)
      val ns = context.actorOf(Props(new Structure(coordinate)))
      ns ! NewProcessingModule(new WaterGenerator(2))
      ns ! NewProcessingModule(new Electrolyser(2))
      ns ! NewProcessingModule(new PowerGenerator(2))
  }

}
