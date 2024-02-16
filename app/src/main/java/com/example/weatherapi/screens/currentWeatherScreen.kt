package com.example.weatherapi.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.weatherapi.WeatherAPIViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CurrentWeatherScreen(viewModel: WeatherAPIViewModel) {
    //WeatherData Variables
    val currentWeatherResponse by viewModel.currentWeatherFlow.collectAsStateWithLifecycle()
    val forecastResponse by viewModel.currentForecastFlow.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistoryFlow.collectAsStateWithLifecycle()

    //Temperature Variables
    val tempFahrenheit = currentWeatherResponse?.current?.temp_f
        ?.roundToInt()
        ?.toString()

    val tempHigh = forecastResponse?.forecast?.forecastday?.get(0)?.day?.maxtemp_f
        ?.roundToInt()
        ?.toString()

    val tempLow = forecastResponse?.forecast?.forecastday?.get(0)?.day?.mintemp_f
        ?.roundToInt()
        ?.toString()

    //Days & Hours Variables
    val amountOfDays = forecastResponse?.forecast?.forecastday?.size
    val amountOfHours = forecastResponse?.forecast?.forecastday?.get(0)?.hour?.size

    //Snackbar Variables
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) {
        Surface(
            color = Color(0xFF1D71F2),
            contentColor = Color.White,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LaunchedEffect(Unit) {
                viewModel.errorMessageFlow.collect { error ->
                    scope.launch {
                        error?.let { snackbarHostState.showSnackbar(error) }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SearchBar(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    searchHistory = searchHistory,
                    onSearch = { viewModel.fetchWeather(it) },
                )

                currentWeatherResponse?.location?.name?.let { locationName ->
                    Text(
                        text = locationName,
                        fontSize = 45.sp,
                        fontWeight = FontWeight.W400,
                    )
                }

                tempFahrenheit?.let { temperature ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = "$temperature°",
                            fontSize = (100.sp),
                            fontWeight = (FontWeight.Light)
                        )
                    }
                }


                Text(
                    text = currentWeatherResponse?.current?.condition?.text ?: "",
                    fontSize = 30.sp
                )


                if (tempHigh != null && tempLow != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "H:$tempHigh°",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Medium

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "L:$tempLow°",
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xFF2E8ACC), shape = RoundedCornerShape(15.dp)),
                ) {
                    Text(
                        text = "Hourly Forecast",
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .alpha(0.6f),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )

                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        color = Color.LightGray,
                        thickness = 0.5.dp
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (amountOfHours != null) {
                            items(count = amountOfHours) { item ->
                                val hourlyWeatherImage =
                                    "https:" + (forecastResponse?.forecast?.forecastday?.get(0)?.hour?.get(
                                        item
                                    )?.condition?.icon)

                                val hourlyTempF = forecastResponse?.forecast
                                    ?.forecastday
                                    ?.get(0)
                                    ?.hour
                                    ?.get(item)
                                    ?.temp_f
                                    ?.roundToInt()
                                    .toString()

                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                                ) {
                                    forecastResponse?.forecast?.forecastday?.get(0)?.hour?.get(item)?.time?.let {
                                        var time = it.drop(11)
                                        if (time[0] == '0') {
                                            time = time.drop(1)
                                        }
                                        if (item == 0) {
                                        } else {
                                            Text(
                                                text = time,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                            Image(
                                                painter = rememberAsyncImagePainter(
                                                    hourlyWeatherImage
                                                ),
                                                contentDescription = "Hourly weather condition icon",
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier.size(56.dp)
                                            )
                                            Text(
                                                text = "$hourlyTempF°",
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color = Color(0xFF2E8ACC), shape = RoundedCornerShape(15.dp))
                ) {
                    if (amountOfDays != null) {
                        items(count = amountOfDays) { item ->
                            val weatherImage =
                                "https:" + (forecastResponse?.forecast?.forecastday?.get(item)?.day?.condition?.icon)

                            val forecastTempHi =
                                forecastResponse?.forecast?.forecastday?.get(item)?.day?.maxtemp_f?.roundToInt()
                                    .toString()

                            val forecastTempLo =
                                forecastResponse?.forecast?.forecastday?.get(item)?.day?.mintemp_f?.roundToInt()
                                    .toString()

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                forecastResponse?.forecast?.forecastday?.get(item)?.let {
                                    Text(
                                        text = it.date.drop(5),
                                        modifier = Modifier.padding(8.dp),
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Image(
                                        painter = rememberAsyncImagePainter(weatherImage),
                                        contentDescription = "Daily weather condition icon",
                                        modifier = Modifier
                                            .size(40.dp)
                                    )
                                    Text(
                                        text = "H: $forecastTempHi°",
                                        modifier = Modifier.padding(12.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "L: $forecastTempLo°",
                                        modifier = Modifier.padding(12.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchBar(
    searchHistory: Set<String>,
    onSearch: (String) -> Unit,
    modifier: Modifier,
) {

    //Search Bar Variables
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        modifier = modifier
            .fillMaxWidth(),
        query = text,
        onQueryChange = {
            text = it
        },
        onSearch = {
            onSearch(it)
            text = ""
            active = false
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close, contentDescription = "Close Icon"
                )
            }
        }
    ) {
        searchHistory.forEach {
            Row(modifier = Modifier
                .padding(14.dp)
                .clickable {
                    onSearch(it)
                    text = ""
                    active = false
                }
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Search history icon",
                    modifier = Modifier
                        .padding(end = 10.dp)
                )
                Text(text = it)
            }
        }
    }
}