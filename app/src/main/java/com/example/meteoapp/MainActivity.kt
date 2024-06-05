package com.example.meteoapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvCurrentTime: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvWeatherDescription: TextView
    private lateinit var tvTemperature: TextView
    private lateinit var timeUpdater: TimeUpdater
    private lateinit var locationProvider: LocationProvider
    private lateinit var weatherInfoProvider: WeatherInfoProvider

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvCity = findViewById(R.id.tvCity)
        tvWeatherDescription = findViewById(R.id.tvWeatherDescription)
        tvTemperature = findViewById(R.id.tvTemperature)

        // Initialisation de TimeUpdater
        timeUpdater = TimeUpdater(tvCurrentTime)
        timeUpdater.startUpdatingTime()

        // Initialisation de WeatherInfoProvider
        weatherInfoProvider = WeatherInfoProvider(this, tvWeatherDescription, tvTemperature)

        // Initialisation de LocationProvider
        locationProvider = LocationProvider(this) { cityName ->
            tvCity.text = cityName
            weatherInfoProvider.fetchWeatherData(cityName)
        }

        // Vérification des permissions et obtention de la localisation
        locationProvider.checkLocationPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LocationProvider.LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationProvider.getLastKnownLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
