package net.swiftspace.core.structure

import akka.actor.{Props, ActorLogging, Actor}
import collection.mutable
import net.swiftspace.core.Simulation.Coordinate
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor
import net.swiftspace.core.structure.StructureManager.StructureInVicinity
import net.swiftspace.core.{Simulation, SwiftspaceAgent}

case class StructureDescriptor(name: String,
                               coordinate: Coordinate,
                               resources: Map[String, Double],
                               processingUnits: List[ProcessingModuleDescriptor])


object Structure {

  case class ReceiveResource(resource: Resource, amount: Double)

  case class DemandResource(resource: Resource, amount: Double)

  case class NewProcessingModule(m: ProcessingModuleDescriptor)

  case class NewStructure(descriptor: StructureDescriptor)

}

/**
 * A structure is a complex building in space that can be upgraded with different kinds of structure modules.
 * Those modules are added as child actors to the structure actor.
 *
 * TODO Structure should be a SwiftspaceAgent!
 */
class Structure(coordinate: Coordinate) extends SwiftspaceAgent {


  // The resources that are (or were) available on this structure.
  val resources = mutable.Map[Resource, Double]().withDefaultValue(0.0)


  override def receive = {
    // Update tick that is forwarded to all children (the structure modules)
    case Simulation.Tick =>
      context.children.foreach(c => c ! Simulation.Tick)

    // Receiving resources from someone of something!
    case Structure.ReceiveResource(resource, amount) =>
      // If resource is already known to this structure then just update the amount of this resource
      // otherwise add the resource to the list of known resources.
      if (resources.contains(resource)) {
        resources.update(resource, resources(resource) + amount)
      } else {
        resources += resource -> amount
      }

    // Someone or something is demanding a resource from the structure.
    case Structure.DemandResource(resource, amount) =>

      // Is this resource available?
      if (resources.contains(resource)) {
        // Send demanded amount of resource to requester if it is available.
        if (resources(resource) >= amount) {
          log.info("Sending " + resource + " to structure...")
          sender ! Structure.ReceiveResource(resource, amount)
          resources.update(resource, resources(resource) - amount)
        } else {
          // Not enough resources available :-(
          // TODO Structure should probably notify the sender that resource isn't available at the moment.
          log.info("Insufficient amount of resource " + resource.name)
        }
      } else {
        // Adding resource to map because it was the first time that someone asked for it.
        // Even if we don't have it, it is always good to know that there is this resource somewhere out there...perhaps.
        resources += resource -> 0.0
      }
    // Hurray! The structure gets a new processing module!
    case Structure.NewProcessingModule(m) =>
      log.info("Adding new processing unit: " + m)
      context.actorOf(Props(new ProcessingModule(m.name, m.input, m.output, m.processingTime, m.capacity)))
    // Structure is asked if it is in the vicinity of some arbitrary point.
    case StructureInVicinity(c, distance) =>
      val d = coordinate.distance(c)
      if (d <= distance) {
        sender ! self
      }

  }
}
