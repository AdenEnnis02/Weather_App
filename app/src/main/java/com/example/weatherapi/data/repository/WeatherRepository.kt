package com.example.weatherapi.data.repository

import com.example.weatherapi.data.CurrentWeatherModel.CurrentWeatherResponse
import com.example.weatherapi.data.ForecastModel.ForecastResponse
import com.example.weatherapi.network.RetrofitInstance

class WeatherRepository() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val apiKey = "9967b7984d38460d98e193559231708"

    suspend fun getCurrentWeatherRemotely(location: String): Result<CurrentWeatherResponse> {
        return runCatching { weatherApi.getCurrentWeather(location, apiKey) }
    }

    suspend fun getCurrentForecastRemotely(location: String): Result<ForecastResponse> {
        return runCatching { weatherApi.getCurrentForecast(location, apiKey, 10) }
    }
}