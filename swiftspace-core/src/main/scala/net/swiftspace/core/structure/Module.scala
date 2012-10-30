package net.swiftspace.core.structure

import akka.actor.{ActorLogging, Actor, ActorRef}

import collection.mutable
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Structure.{DemandResource, ReceiveResource}


/**
 * Part of a structure.
 */
abstract class Module extends Actor with ActorLogging

/**
 * Can process resources.
 * @param input the input resources and how much is needed to produce one unit of the resulting resource
 * @param output the resource that is being produced
 * @param processingRate how much is produced with one input unit
 * @param processingTime how long it takes to produce one unit
 * @param capacity how much the processing unit can store
 */
class ProcessingModule(
                        input: Seq[(Resource, Double)],
                        output: Resource,
                        processingRate: Double,
                        processingTime: Double,
                        capacity: Double)
  extends Module {

  import net.swiftspace.core.Simulation.Tick

  val resources = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)
  input.foreach(r => resources += r._1 -> 0.0)

  var result = 0.0
  var counter = 0
  var storage = 0.0

  def processResources(): Unit = {
    log.info("processing " + input + " to " + output)
    if (input.foldLeft(true)((a, b) => a && resources.get(b._1).get >= b._2)) {
      storage += processingRate
      log.info(output + " available: " + storage)
      input.foreach(r => resources.update(r._1, resources.get(r._1).get - r._2))
    } else {
      input.foreach(r => {
        if (resources.get(r._1).get < r._2) {
          log.info("Demanding resource from main structure: " + r._1.name)
          context.parent ! DemandResource(r._1, r._2 * 10)
        }
      })
    }
  }

  def receive = {
    case Tick =>
      counter += 1
      if (counter >= processingTime) {
        processResources()
        counter = 0
      }
      if (storage > 0.8 * capacity) {
        context.parent ! ReceiveResource(output, storage)
        storage = 0.0
      }
    case ReceiveResource(resource, amount) =>
      if (resources.contains(resource)) {
        resources.update(resource, resources.get(resource).get + amount)
      } else {
        log.error("Module cannot process " + resource)
      }

      log.debug("Amount of " + resource + " available: " + resources.get(resource))
  }
}

object Module {

  case class ModuleDescriptor( input: Seq[(Resource, Double)],
                          output: Resource,
                          processingRate: Double,
                          processingTime: Double,
                          capacity: Double)

  class WaterProcessor(level: Int = 1) extends ModuleDescriptor(
    Seq[(Resource, Double)]((Resource("Oxygen"), 1), (Resource("Hydrogen"), 2)),
    Resource("Water"),
    1, 50.0 / level, 3 * level)

}
