package com.example.weatherapi.data.ForecastModel

data class ForecastResponse(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)