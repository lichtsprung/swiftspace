import net.swiftspace.core.config.Config
import net.swiftspace.core.processing.Resource
import net.swiftspace.core.structure.Module.ProcessingModuleDescriptor

new Config {
  processing += ProcessingModuleDescriptor(
    name = "Power Generator",

    input = List(
      (Resource("Deuterium Oxide"), 2),
      (Resource("Power"), 2.5)
    ),

    output = List(
      (Resource("Power"), 6),
      (Resource("Helium"), 0.3)
    ),

    processingTime = 0.5,
    capacity = 500
  )

  processing += ProcessingModuleDescriptor(
    name = "Water Generator",

    input = List(
      (Resource("Oxygen"), 2),
      (Resource("Hydrogen"), 1),
      (Resource("Power"), 0.3)
    ),

    output = List(
      (Resource("Water"), 1)
    ),

    processingTime = 0.1,
    capacity = 500
  )
}



