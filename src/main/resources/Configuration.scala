import collection.immutable.HashMap
import net.swiftspace.core.config.Config
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.Simulation.Coordinate
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor
import net.swiftspace.core.structure.StructureDescriptor

new Config {


  def initProcessingUnits() {
    processing += "Power Generator" -> ProcessingModuleDescriptor(
      name = "Power Generator",

      input = List(
        ("Deuterium Oxide", 2),
        ("Power", 2.5)
      ),

      output = List(
        ("Power", 6),
        ("Helium", 0.3)
      ),

      processingTime = 0.5,
      capacity = 500
    )

    processing += "Water Generator" -> ProcessingModuleDescriptor(
      name = "Water Generator",

      input = List(
        ("Oxygen", 2),
        ("Hydrogen", 1),
        ("Power", 0.3)
      ),

      output = List(
        ("Water", 1)
      ),

      processingTime = 0.1,
      capacity = 500
    )
  }

  def initResources() {
    resources += "Deuterium Oxide" -> Resource(
      name = "Deuterium Oxide",
      description = "Also known as Heavy Water",
      mass = 1.0,
      characteristics = List("liquid", "absorb radioactivity"))
    resources += "Water" -> Resource(
      name = "Water",
      description = "Water is essential...",
      mass = 1.0,
      characteristics = List("liquid"))
    resources += "Oxygen" -> Resource(
      name = "Oxygen",
      description = "O2 is needed for human respiration.",
      mass = 0.01,
      characteristics = List("gaseous", "inflamable"))
    resources += "Hydrogen" -> Resource(
      name = "Hydrogen",
      description = "The lightest chemical element.",
      mass = 0.0001,
      characteristics = List("gaseous", "inflamable"))
    resources += "Power" -> Resource(
      name = "Power",
      description = "Electric power is needed almost anywhere",
      mass = 0.0,
      characteristics = List())
  }

  def initStartupStructures() {
    structures += "Deep Space Nine" -> StructureDescriptor(
      name = "Deep Space Nine",
      coordinate = Coordinate(0, 0, 0),
      resources = HashMap("Water" -> 3),
      processingUnits = List(processing.get("Power Generator").get)
    )
  }
}



