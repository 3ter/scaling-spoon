package com.example.scaling_spoon

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.math.pow
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    // The "main()" method for Android apps
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // You'll have to start somewhere
        setContentView(R.layout.activity_main)

        // "R" does seem to stand for "Resources"
        val calculateWaterButton: Button = findViewById(R.id.button)

        calculateWaterButton.setOnClickListener {
            Toast.makeText(this, "Splash...", Toast.LENGTH_SHORT).show()

            val result: TextView = findViewById(R.id.result)
            result.text = ""
            lifecycleScope.launch(Dispatchers.Main) {
                result.text =
                    "${HumidityCalculator().getHumidityInsideFromOutsideWater(HumidityCalculator().getWeatherData())}%"
            }
        }
    }
}

class HumidityCalculator() {

    fun getHumidityInsideFromOutsideWater(weatherData: WeatherData): Int {
        val insideTemp = 21.5

        val outsideTemp = weatherData.temp
        val outsideHumidity = weatherData.humidity

        // Measured in [g/m^3] from https://rechneronline.de/barometer/luftfeuchtigkeit.php#ref
        val absoluteHumidity =
            (6.112 * Math.E.pow((17.67 * outsideTemp) / (outsideTemp + 243.5)) * outsideHumidity * 2.1674) / (273.15 + outsideTemp)

        val insideHumidity =
            (absoluteHumidity * (273.15 + insideTemp)) / (6.112 * Math.E.pow((17.67 * insideTemp) / (insideTemp + 243.5)) * 2.1674)

        return insideHumidity.roundToInt()
    }

    data class WeatherData(val temp: Double, val humidity: Int)

    suspend fun getWeatherData(): WeatherData {
        val client = HttpClient(CIO)
        // TODO Add api key via config (and store it responsibly on the device)
        val apiKey = ""
        val response: HttpResponse = client.request(
            "https://api.openweathermap.org/data/2.5/weather?" + "lat=50.006628484827594&" + "lon=8.262631638921002&units=metric& " + "appid=${apiKey}"
        ) {
            method = HttpMethod.Get
        }
        val body: String = response.body()
        val json = JSONObject(body).getJSONObject("main")
        return WeatherData(json.getDouble("temp"), json.getInt("humidity"))
    }
}