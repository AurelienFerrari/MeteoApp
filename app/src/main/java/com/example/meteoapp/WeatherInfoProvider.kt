package com.example.meteoapp

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class WeatherInfoProvider(
    private val context: Context,
    private val tvWeatherDescription: TextView,
    private val tvTemperature: TextView
) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherApi = retrofit.create(WeatherApi::class.java)

    fun fetchWeatherData(cityName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApi.getCurrentWeather(cityName, "34ea1736d26f6bc1dcc15a718d72103c")
                withContext(Dispatchers.Main) {
                    updateWeatherUI(response)
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to fetch weather data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to fetch weather data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateWeatherUI(weatherResponse: WeatherResponse) {
        val weatherDescription = weatherResponse.weather[0].description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        val temperatureInKelvin = weatherResponse.main.temp
        val temperatureInCelsius = temperatureInKelvin - 273.15
        val temperature = String.format("%.0f°C", temperatureInCelsius)  // Rounded to the nearest integer

        tvWeatherDescription.text = weatherDescription
        tvTemperature.text = temperature
    }

}
