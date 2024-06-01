package com.example.meteoapp

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val name: String
    )

    data class Weather(
        val description: String,
        val icon: String
    )

    data class Main(
        val temp: Float,
        val temp_min: Float,
        val temp_max: Float
)


