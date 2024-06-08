package com.example.meteoapp

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class Weather(
    val description: String
)

data class Main(
    val temp: Double
)

data class Wind(
    val speed: Double
)