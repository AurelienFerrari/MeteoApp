package com.example.meteoapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ImageView

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
    private lateinit var etCity: EditText
    private lateinit var btnFetchWeather: Button
    private lateinit var timeUpdater: TimeUpdater
    private lateinit var locationProvider: LocationProvider
    private lateinit var weatherInfoProvider: WeatherInfoProvider
    private lateinit var tvWindSpeed: TextView
    private lateinit var ivWeatherIcon: ImageView
    private lateinit var tvPressure: TextView
    private lateinit var tvHumidity: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvCity = findViewById(R.id.tvCity)
        tvWeatherDescription = findViewById(R.id.tvWeatherDescription)
        tvTemperature = findViewById(R.id.tvTemperature)
        etCity = findViewById(R.id.etCity)
        ivWeatherIcon = findViewById(R.id.weather_icon)
        btnFetchWeather = findViewById(R.id.btnFetchWeather)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)
        tvPressure = findViewById(R.id.tvPressure)
        tvHumidity = findViewById(R.id.tvHumidity)

        // Initialisation de TimeUpdater
        timeUpdater = TimeUpdater(tvCurrentTime)
        timeUpdater.startUpdatingTime()

        // Initialisation de WeatherInfoProvider
        weatherInfoProvider = WeatherInfoProvider(this, tvWeatherDescription, tvTemperature,tvWindSpeed,ivWeatherIcon,tvPressure,tvHumidity)

        // Initialisation de LocationProvider
        locationProvider = LocationProvider(this) { cityName ->
            tvCity.text = cityName
            weatherInfoProvider.fetchWeatherData(cityName)
        }

        // Vérification des permissions et obtention de la localisation
        locationProvider.checkLocationPermission()

        // Fetch weather data for entered city name on button click
        btnFetchWeather.setOnClickListener {
            val cityName = etCity.text.toString()
            if (cityName.isNotEmpty()) {
                tvCity.text = cityName
                weatherInfoProvider.fetchWeatherData(cityName)
            } else {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            }
        }
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