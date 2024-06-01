// MainActivity.kt
package com.example.meteoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchWeather("Paris")
    }

    private fun fetchWeather(city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(city, "34ea1736d26f6bc1dcc15a718d72103c")
                withContext(Dispatchers.Main) {
                    // Afficher les résultats dans la console
                    println("City: ${response.name}")
                    println("Description: ${response.weather[0].description}")
                    println("Temperature: ${response.main.temp}°C")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
