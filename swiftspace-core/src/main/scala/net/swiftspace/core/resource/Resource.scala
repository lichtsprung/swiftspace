package net.swiftspace.core.resource

/**
 * A Resource Definition.
 * @param name the name of the resource
 * @param description a short description of the resource
 * @param mass the mass of the resource
 * @param baseValue the base value of the resource
 * @param meltingTemperature the melting temperature
 * @param boilingTemperature the boiling temperature
 * @param solidCharacteristics the characteristics the resource has when solid
 * @param liquidCharacteristics the characteristics the resource has when liquid
 * @param gaseousCharacteristics the characteristics the resource has when gaseous
 */
case class Resource(
                     name: String,
                     description: String,
                     mass: Double,
                     baseValue: Double,
                     meltingTemperature: Double,
                     boilingTemperature: Double,
                     solidCharacteristics: Characteristics,
                     liquidCharacteristics: Characteristics,
                     gaseousCharacteristics: Characteristics
                     )


