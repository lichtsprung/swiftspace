package net.swiftspace.core.structure

import akka.actor.{ActorLogging, Props, Actor}
import net.swiftspace.core.structure.Structure.{NewProcessingModule, NewStructure}
import xml.XML
import java.io.File
import com.twitter.util.Eval
import net.swiftspace.core.config.Config


object StructureManager {

}

/**
 * Manages the structures inside the simulation
 */
class StructureManager extends Actor with ActorLogging {

  import net.swiftspace.core.Simulation.Tick

  val test = new Eval()


  def receive = {
    case Tick =>
      context.children.foreach(c => c ! Tick)
    case NewStructure(coordinate) =>
      log.info("Spawning new Structure: " + coordinate)
      val ns = context.actorOf(Props(new Structure(coordinate)))
  }

}
