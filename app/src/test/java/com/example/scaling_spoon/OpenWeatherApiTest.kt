package com.example.scaling_spoon

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class OpenWeatherApiTest {
    @Test
    fun getWeatherData_checkTempAndHumidity() = runBlocking {
        val humidityCalculator = HumidityCalculator()
        val weatherData = humidityCalculator.getWeatherData()
        assertTrue(weatherData.temp > -30 && weatherData.temp < 60)
        assertTrue(weatherData.humidity >= 0)
    }
}