package net.swiftspace.core.structure

import akka.actor.{Props, ActorLogging, Actor}
import collection.mutable
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.Simulation.Coordinate


object Structure {

  case class ReceiveResource(resource: Resource, amount: Double)

  case class DemandResource(resource: Resource, amount: Double)

  case class NewProcessingModule(input: Seq[(Resource, Double)],
                                 output: Resource,
                                 processingRate: Double,
                                 processingTime: Double,
                                 capacity: Double)

  case class NewStructure(coordinates: Coordinate)

}

/**
 * A structure is a complex building in space.
 */
class Structure(coordinate: Coordinate) extends Actor with ActorLogging {

  import net.swiftspace.core.Simulation._
  import net.swiftspace.core.structure.Structure._

  val resources = mutable.HashMap[Resource, Double]()


  def receive = {
    case Tick =>
      context.children.foreach(c => c ! Tick)
    case ReceiveResource(resource, amount) =>
      resources.update(resource, resources.get(resource).get + amount)
      log.info("Amount of " + resource + " available: " + resources.get(resource))
    case DemandResource(resource, amount) =>
      if (resources.get(resource).get >= amount) {
        sender ! ReceiveResource(resource, amount)
        resources.update(resource, resources.get(resource).get - amount)
      }
      log.info("Amount of " + resource + " available: " + resources.get(resource))
    case NewProcessingModule(input, output, processingRate, processingTime, capacity) =>
      log.info("Adding new processing unit")
      input.foreach(resource => resources += resource._1 -> 200)
      resources += output -> 0
      context.actorOf(Props(new ProcessingModule(input, output, processingRate, processingTime, capacity)))

  }
}
