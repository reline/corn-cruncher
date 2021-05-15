package com.github.reline.corncruncher

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class CalculatorTest {

    @Test
    fun testRefill() = runBlocking {
        val result = Calculator.determineFuelRequired(
                12.4f,
                20f,
                27f,
                10f,
                52f,
                30f,
        )
        assertEquals(5.0098877, result.first.gallons)
        assertEquals(4.8926263, result.second.gallons)
    }

    @Test
    fun testSimple() {
        val fuelTwoGallons = Calculator.determineFuelRequired(
                fuelOne = Fuel(ethanolContent = 10f, gallons = 5f),
                fuelTwoEthanolContent = 52f,
                fuelTargetEthanolContent = 30f
        )
        assertEquals(4.545455f, fuelTwoGallons)
    }

    @Test
    fun testComplex() {
        val result = Calculator.determineFuelRequired(
                fuelRemaining = Fuel(gallons = 2.0f, ethanolContent = 27f),
                fuelOne = Fuel(ethanolContent = 10f, gallons = 5f),
                52f,
                30f
        )
        assertEquals(4.8181825f, result)
    }

    @Test
    fun sanityTest() {
        assertEquals(Calculator.determineFuelRequired(
                fuelRemaining = Fuel(gallons = 0.0f, ethanolContent = 27f),
                fuelOne = Fuel(ethanolContent = 10f, gallons = 5f),
                52f,
                30f
        ), Calculator.determineFuelRequired(
                fuelOne = Fuel(ethanolContent = 10f, gallons = 5f),
                fuelTwoEthanolContent = 52f,
                fuelTargetEthanolContent = 30f
        ))
    }
}
