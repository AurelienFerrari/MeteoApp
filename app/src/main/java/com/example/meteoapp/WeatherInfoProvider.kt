package com.example.meteoapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class WeatherInfoProvider(
    private val context: Context,
    private val tvWeatherDescription: TextView,
    private val tvTemperature: TextView,
    private val tvWindSpeed: TextView,
) {

    // Dictionnaire de traductions (toutes les clés en minuscules)
    private val weatherDescriptionTranslations = mapOf(
        "clear sky" to "Ciel dégagé",
        "few clouds" to "Quelques nuages",
        "scattered clouds" to "Nuages dispersés",
        "broken clouds" to "Nuages fragmentés",
        "shower rain" to "Pluie averse",
        "rain" to "Pluie",
        "thunderstorm" to "Orage",
        "snow" to "Neige",
        "mist" to "Brume",
        "overcast clouds" to "Nuageux",
        "moderate rain" to "Pluie modérée",
        "light rain" to "Pluie fine"

    )

    fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = weatherApi.getCurrentWeather(cityName, "34ea1736d26f6bc1dcc15a718d72103c")
                withContext(Dispatchers.Main) {
                    updateWeatherUI(response)
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
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

        val translatedDescription = weatherDescriptionTranslations[weatherDescription.lowercase(Locale.getDefault())] ?: weatherDescription

        val temperatureInKelvin = weatherResponse.main.temp
        val temperatureInCelsius = temperatureInKelvin - 273.15
        val temperature = String.format("%.0f°C", temperatureInCelsius)

        val windSpeedInMetersPerSecond = weatherResponse.wind.speed
        val windSpeedInKilometersPerHour = windSpeedInMetersPerSecond * 3.6
        val windSpeed = String.format("%.1f km/h", windSpeedInKilometersPerHour)

        tvWeatherDescription.text = translatedDescription
        tvTemperature.text = temperature
        tvWindSpeed.text = windSpeed



        Log.d("WeatherUpdate", "Description: $translatedDescription, Temperature: $temperature, Wind Speed: $windSpeed")
    }


}
