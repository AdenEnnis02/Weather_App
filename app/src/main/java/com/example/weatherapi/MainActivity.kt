package com.example.weatherapi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.weatherapi.locationtracking.LocationService
import com.example.weatherapi.screens.CurrentWeatherScreen
import com.example.weatherapi.ui.theme.WeatherAPITheme

class MainActivity : ComponentActivity() {
    private val viewModel: WeatherAPIViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )

        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }

        setContent {
            WeatherAPITheme {
                CurrentWeatherScreen(viewModel)
            }
        }
    }
}