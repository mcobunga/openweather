package com.bonface.openweather.utils

import com.bonface.openweather.R
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

fun getTemperature(temp: Double?): String {
    return "${temp?.roundToInt()}°"
}

fun getCurrentWeatherImage(weatherId: Int?): Int {
    return currentWeatherImage(weatherId.toString())
}

fun getCurrentWeatherBackgroundColor(weatherId: Int?): Int {
    return currentWeatherBackgroundColor(weatherId.toString())
}

fun getForecastWeatherIcon(weatherId: Int?): Int {
    return forecastWeatherIcon(weatherId.toString())
}

fun roundOffLatLonDecimal(number: Double): Double {
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}

private fun currentWeatherImage(id: String): Int {
    return when {
        // id prefix 2xx is thunderstorm
        id.startsWith("2", true) -> R.drawable.forest_rainy
        //id prefix 3xx is drizzle
        id.startsWith("3", true) -> R.drawable.forest_rainy
        //id prefix 5xx is rain
        id.startsWith("5", true) -> R.drawable.forest_rainy
        //id prefix 6xx is snow
        id.startsWith("6", true) -> R.drawable.forest_rainy
        //id prefix 7xx is atmosphere
        id.startsWith("7", true) -> R.drawable.forest_sunny
        //id 800 is clear
        (id == "800") -> R.drawable.forest_cloudy
        //id prefix 80x is clouds
        (id.toInt() > 800) -> R.drawable.forest_cloudy
        else -> R.drawable.forest_sunny
    }
}

private fun currentWeatherBackgroundColor(id: String): Int {
    return when {
        // id prefix 2xx is thunderstorm
        id.startsWith("2", true) -> R.color.colorRainy
        //id prefix 3xx is drizzle
        id.startsWith("3", true) -> R.color.colorRainy
        //id prefix 5xx is rain
        id.startsWith("5", true) -> R.color.colorRainy
        //id prefix 6xx is snow
        id.startsWith("6", true) -> R.color.colorRainy
        //id prefix 7xx is atmosphere
        id.startsWith("7", true) -> R.color.colorSunny
        //id 800 is clear
        (id == "800") -> R.color.colorCloudy
        //id prefix 80x is clouds
        (id.toInt() > 800) -> R.color.colorCloudy
        else -> R.color.colorSunny
    }
}

private fun forecastWeatherIcon(id: String): Int {
    return when {
        // id prefix 2xx is thunderstorm
        id.startsWith("2", true) -> R.drawable.rain
        //id prefix 3xx is drizzle
        id.startsWith("3", true) -> R.drawable.rain
        //id prefix 5xx is rain
        id.startsWith("5", true) -> R.drawable.rain
        //id prefix 6xx is snow
        id.startsWith("6", true) -> R.drawable.rain
        //id prefix 7xx is atmosphere
        id.startsWith("7", true) -> R.drawable.partlysunny
        //id 800 is clear
        (id == "800") -> R.drawable.clear
        //id prefix 80x is clouds
        (id.toInt() > 800) -> R.drawable.ic_baseline_cloud_queue_24
        else -> R.drawable.ic_baseline_cloud_queue_24
    }
}