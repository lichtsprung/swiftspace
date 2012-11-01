package net.swiftspace.core.processing

/**
 * A processable resource.
 */
case class Resource(
                     name: String,
                     description: String,
                     mass: Double,
                     characteristics: List[String]
                     )
