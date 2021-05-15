package com.github.reline.corncruncher

data class Fuel(val ethanolContent: Float, val gallons: Float) {
    val ethanolPercentage: Float by lazy { ethanolContent.percentage }
    val ethanolGallons: Float by lazy { ethanolPercentage * gallons }
}

val Float.percentage: Float get() = this / 100f

object Calculator {

    fun determineFuelRequired(
            tankSizeGallons: Float,
            currentFuel: Float,
            currentEthanolContent: Float,
            fuelOneEthanolContent: Float,
            fuelTwoEthanolContent: Float,
            fuelTargetEthanolContent: Float,
    ): Pair<Fuel, Fuel> {
        val fuelRemaining = Fuel(
                gallons = currentFuel.percentage * tankSizeGallons,
                ethanolContent = currentEthanolContent
        )
        val maxFuelGallons = tankSizeGallons - fuelRemaining.gallons
        var input = tankSizeGallons - fuelRemaining.gallons
        while (input >= 0) {
            val fuelOne = Fuel(gallons = input, ethanolContent = fuelOneEthanolContent)
            val result = determineFuelRequired(
                    fuelRemaining = Fuel(gallons = currentFuel.percentage * tankSizeGallons, ethanolContent = currentEthanolContent),
                    fuelOne,
                    fuelTwoEthanolContent,
                    fuelTargetEthanolContent,
            )
            val fuelTwo = Fuel(gallons = result, ethanolContent = fuelTwoEthanolContent)
            if (fuelOne.gallons + fuelTwo.gallons <= maxFuelGallons) {
                return Pair(fuelOne, fuelTwo)
            }
            input -= .01f
        }
        throw IllegalArgumentException("impossibru")
    }

    fun determineFuelRequired(
            fuelRemaining: Fuel = Fuel(0f, 0f),
            fuelOne: Fuel,
            fuelTwoEthanolContent: Float,
            fuelTargetEthanolContent: Float,
    ): Float {
        val fuelTargetEthanolPercentage = fuelTargetEthanolContent.percentage
        val ethanolGallons = fuelTargetEthanolPercentage * fuelRemaining.gallons + fuelTargetEthanolPercentage * fuelOne.gallons - fuelRemaining.ethanolGallons - fuelOne.ethanolGallons
        return ethanolGallons / (fuelTwoEthanolContent.percentage - fuelTargetEthanolPercentage)
    }
}
