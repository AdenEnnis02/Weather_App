package com.example.weatherapi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapi.data.CurrentWeatherModel.CurrentWeatherResponse
import com.example.weatherapi.data.ForecastModel.ForecastResponse
import com.example.weatherapi.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherAPIViewModel : ViewModel() {
    companion object {
        const val WEATHER_LOCATION = "Paris"
        const val ERROR_MESSAGE = "Location not found."
    }

    private val repository = WeatherRepository()

    private val _currentWeatherFlow = MutableStateFlow<CurrentWeatherResponse?>(null)
    val currentWeatherFlow = _currentWeatherFlow.asStateFlow()

    private val _currentForecastFlow = MutableStateFlow<ForecastResponse?>(null)
    val currentForecastFlow = _currentForecastFlow.asStateFlow()

    private val _errorMessageFlow = MutableSharedFlow<String?>()
    val errorMessageFlow = _errorMessageFlow.asSharedFlow()

    private val _searchHistoryFlow = MutableStateFlow(
        mutableSetOf(
            "Paris",
            "Cupertino",
            "New York",
            "San Diego"
        )
    )
    val searchHistoryFlow = _searchHistoryFlow.asStateFlow()

    init {

        fetchCurrentWeather(WEATHER_LOCATION)
        fetchCurrentForecast(WEATHER_LOCATION)
    }

    fun fetchWeather(location: String) {
        fetchCurrentForecast(location)
        fetchCurrentWeather(location)
    }

    private fun fetchCurrentWeather(location: String) = viewModelScope.launch {
        repository.getCurrentWeatherRemotely(location)
            .onSuccess { currentWeatherResponse ->
                Log.d("WEATHER API VIEW MODEL", "SUCCESS: $currentWeatherResponse")
                _currentWeatherFlow.value = currentWeatherResponse
                _searchHistoryFlow.value.add(currentWeatherResponse.location.name)
            }.onFailure { throwable ->
                _errorMessageFlow.emit(ERROR_MESSAGE)
                Log.d("WEATHER API VIEW MODEL", "FAILURE: ${throwable.message}")
            }
    }

    private fun fetchCurrentForecast(location: String) = viewModelScope.launch {
        repository.getCurrentForecastRemotely(location)
            .onSuccess { forecastResponse ->
                Log.d("WEATHER API VIEW MODEL", "SUCCESS: $forecastResponse")
                _currentForecastFlow.value = forecastResponse
            }.onFailure { throwable ->
                Log.d("WEATHER API VIEW MODEL", "FAILURE: ${throwable.message}")
            }
    }
}