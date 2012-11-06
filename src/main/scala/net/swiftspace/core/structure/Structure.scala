package net.swiftspace.core.structure

import akka.actor.{Props, ActorLogging, Actor}
import collection.mutable
import net.swiftspace.core.Simulation.Coordinate
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor

case class StructureDescriptor(name: String,
                               val coordinate: Coordinate,
                               val resources: Map[String, Double],
                               val processingUnits: List[ProcessingModuleDescriptor])


object Structure {

  case class ReceiveResource(resource: Resource, amount: Double)

  case class DemandResource(resource: Resource, amount: Double)

  case class NewProcessingModule(m: ProcessingModuleDescriptor)

  case class NewStructure(descriptor: StructureDescriptor)

}

/**
 * A structure is a complex building in space.
 */
class Structure(coordinate: Coordinate) extends Actor with ActorLogging {

  import net.swiftspace.core.Simulation._
  import net.swiftspace.core.structure.Structure._

  val resources = mutable.Map[Resource, Double]().withDefaultValue(0.0)


  def receive = {
    case Tick =>
      context.children.foreach(c => c ! Tick)
    case ReceiveResource(resource, amount) =>
      if (resources.contains(resource)) {
        resources.update(resource, resources.get(resource).get + amount)
      } else {
        resources += resource -> amount
      }
      log.info("Resource " + resource + " received: " + resources(resource))
    case DemandResource(resource, amount) =>
      log.info("getting demand request from " + sender)
      if (resources.contains(resource)) {
        if (resources(resource) >= amount) {
          sender ! ReceiveResource(resource, amount)
          resources.update(resource, resources.get(resource).get - amount)
        } else {
          log.info("Insufficient amount of resource " + resource.name)
        }
      } else {
        resources += resource -> 0.0
      }
    case NewProcessingModule(m) =>
      log.info("Adding new processing unit")
      context.actorOf(Props(new ProcessingModule(m.name, m.input, m.output, m.processingTime, m.capacity)))

  }
}
