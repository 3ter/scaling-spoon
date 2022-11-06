package com.example.scaling_spoon

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CalculateHumidityTest {
    @Test
    fun getHumidityInsideFromOutsideWater_verifyValues() = runBlocking {
        val humidityCalculator = HumidityCalculator()

        // The following values assume an inside temp of 21.5°C
        assertEquals(
            25, humidityCalculator.getHumidityInsideFromOutsideWater(
                HumidityCalculator.WeatherData(
                    10.0, 50
                )
            )
        )
    }
}