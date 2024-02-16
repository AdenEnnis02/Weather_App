package com.example.weatherapi.network

import com.example.weatherapi.data.CurrentWeatherModel.CurrentWeatherResponse
import com.example.weatherapi.data.ForecastModel.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") latLongString: String,
        @Query("key") apiKey: String,
    ): CurrentWeatherResponse
    @GET("forecast.json")
    suspend fun getCurrentForecast(
        @Query("q") latLongString: String,
        @Query("key") apiKey: String,
        @Query("Days") additionalDays: Int,
    ): ForecastResponse
}