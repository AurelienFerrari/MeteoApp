package com.example.meteoapp

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind
)

data class Main(
    val temp: Double,
    val pressure: Double,
    val humidity: Int
)

data class Weather(
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double
)

