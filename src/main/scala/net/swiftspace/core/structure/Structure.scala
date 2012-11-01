package net.swiftspace.core.structure

import akka.actor.{Props, ActorLogging, Actor}
import collection.mutable
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.Simulation.Coordinate
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor


object Structure {

  case class ReceiveResource(resource: Resource, amount: Double)

  case class DemandResource(resource: Resource, amount: Double)

  case class NewProcessingModule(m: ProcessingModuleDescriptor)

  case class NewStructure(coordinates: Coordinate)

}

/**
 * A structure is a complex building in space.
 */
class Structure(coordinate: Coordinate) extends Actor with ActorLogging {

  import net.swiftspace.core.Simulation._
  import net.swiftspace.core.structure.Structure._

  val resources = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)


  def receive = {
    case Tick =>
      context.children.foreach(c => c ! Tick)
      log.info("\nAvailable resources: " + resources)
    case ReceiveResource(resource, amount) =>
      if (resources.contains(resource)) {
        resources.update(resource, resources.get(resource).get + amount)
      } else {
        resources += resource -> amount
      }
    case DemandResource(resource, amount) =>
      if (resources.contains(resource)) {
        if (resources.get(resource).get >= amount) {
          sender ! ReceiveResource(resource, amount)
          resources.update(resource, resources.get(resource).get - amount)
        } else {
          log.info("Insufficient amount of resource " + resource.name)
        }
      } else {
        resources += resource -> 1000.0
        sender ! ReceiveResource(resource, amount)
        resources.update(resource, resources.get(resource).get - amount)
      }
    case NewProcessingModule(m) =>
      log.info("Adding new processing unit")
      context.actorOf(Props(new ProcessingModule(m.name, m.input, m.output, m.processingTime, m.capacity)))

  }
}
