package net.swiftspace.core.structure

import akka.actor.{ActorLogging, Actor}
import collection.mutable
import java.util.concurrent.TimeUnit
import net.swiftspace.core.Simulation
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Structure.{DemandResource, ReceiveResource}
import net.swiftspace.core.Simulation._


/**
 * Part of a structure.
 */
abstract class Module extends Actor with ActorLogging

/**
 * Can process resources.
 * @param name the name of this unit
 * @param input the input resources and how much is needed to produce one unit of the resulting resource
 * @param output the resource that is being produced
 * @param processingTime how long it takes to produce one unit
 * @param capacity how much the processing unit can store
 */
class ProcessingModule(name: String,
                       input: List[(String, Double)],
                       output: List[(String, Double)],
                       processingTime: Double,
                       capacity: Double)
  extends Module {

  import net.swiftspace.core.Simulation.Tick

  //  Resources and the available units stored inside the unit.
  val resources = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)

  //  Filling the resource map with content.
  input.foreach(r => resources += getResource(r._1) -> 0.0)

  //  The resources that are being produced by this module and the available amounts.
  val produces = mutable.HashMap[Resource, Double]().withDefaultValue(0.0)

  //  Filling the produce map with content.
  output.foreach(r => produces += getResource(r._1) -> 0.0)


  //  Tick counter that is incremented on every global tick message. It is used to determine when the processResource
  //  function is called.
  var counter = 0.0

  //  Counter for the amount of stored produces. When the max capacity is reached, the module stops producing goods.
  var storage = 0.0

  /**
   * Called when the processing timer is full and the module can produce new units of goods.
   * In that process the input resources are consumed while the produces are being created and stored inside this
   * module.
   */
  def processResources() {
    // Controls whether there are enough resources available to produce a unit by checking the available amount of every
    // ingredient. One unit of the resulting good is created and stored if all resources are sufficiently available.
    if (input.foldLeft(true)((a, b) => a && resources(getResource(b._1)) >= b._2)) {
      // Adding the the newly created good to the internal storage.
      storage += output.foldLeft(0.0)((a, b) => a + b._2)

      // Removing used input resources.
      input.foreach(r => resources.update(getResource(r._1), resources.get(getResource(r._1)).get - r._2))

      // Adding the created goods to the map.
      output.foreach(r => produces.update(getResource(r._1), produces.get(getResource(r._1)).get + r._2))
      produces.foreach(r => log.info("Resource: " + r._1 + " :: " + r._2))
    } else {
      // Demanding new resources from the station if not enough are available.
      input.foreach(r => {
        // Checking if the resource r needs restocking.
        if (resources(getResource(r._1)) < r._2) {
          // Sending demand to station (the parent of this actor)
          context.parent ! DemandResource(getResource(r._1), r._2 * 10)
        }
      })
    }
  }


  def receive = {
    // Global update tick
    case Tick =>
      // Adding tick rate to counter. When the counter reaches the processingTime threshold, the processing function is
      // called.
      counter += Simulation.tickRate.toUnit(TimeUnit.SECONDS)
      if (counter >= processingTime) {
        processResources()
        counter = 0
      }
      // If internal storage is almost full (80%), the station gets a notification that it will receive new resources
      // from this unit.
      if (storage > 0.8 * capacity) {
        produces.foreach(r => {
          context.parent ! ReceiveResource(r._1, r._2)
          produces.update(r._1, 0)
        })
        storage = 0.0
      }

    // Handling new resources from the structure
    case ReceiveResource(resource, amount) =>
      // Checking whether this unit can process the received resource. If it can't, send it back to the structure.
      if (resources.contains(resource)) {
        resources.update(resource, resources.get(resource).get + amount)
      } else {
        log.error("Module cannot process " + resource)
        context.parent ! ReceiveResource(resource, amount)
      }
  }
}

object Module {

  /**
   * This class is used as a descriptor for a processing unit in the simulation. It can be sent between actors and used
   * as a reference to create a new processing module.
   *
   * @param input the input resources
   * @param output the resources that are being produced
   * @param processingTime the time it takes to create 1 unit of the output
   * @param capacity the maximum storage capacity before it is delivered to the main structure
   */
  case class ProcessingModuleDescriptor(name: String,
                                        input: List[(String, Double)],
                                        output: List[(String, Double)],
                                        processingTime: Double,
                                        capacity: Double)

}
