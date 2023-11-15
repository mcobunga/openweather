package com.bonface.openweather.data.model

import android.os.Parcelable
import androidx.annotation.ColorInt
import com.bonface.openweather.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@JsonClass(generateAdapter = true)
@Parcelize
data class CurrentWeather(
    @Json(name="visibility") val visibility: Long? = null,
    @Json(name="timezone") val timezone: Long? = null,
    @Json(name="main") val main: Main? = null,
    @Json(name="clouds") val clouds: Clouds? = null,
    @Json(name="sys") val sys: Sys? = null,
    @Json(name="dt") val dt: Long? = null,
    @Json(name="coord") val coord: Coord? = null,
    @Json(name="weather") val weather: List<Weather>? = null,
    @Json(name="name") val name: String? = null,
    @Json(name="cod") val cod: Long? = null,
    @Json(name="id") val id: Long? = null,
    @Json(name="base") val base: String? = null,
    @Json(name="wind") val wind: Wind? = null,
) : Parcelable {
    fun lastUpdated(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return run {
            val netDate = Date(dt!! * 1000)
            sdf.format(netDate)
        }
    }

    fun getCurrentWeatherImage(): Int {
        return currentWeatherImage(weather?.firstOrNull()?.id.toString())
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

    fun getCurrentWeatherBackgroundColor(): Int {
        return currentWeatherBackgroundColor(weather?.firstOrNull()?.id.toString())
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
            id.startsWith("7", true) -> R.color.colorRainy
            //id 800 is clear
            (id == "800") -> R.color.colorSunny
            //id prefix 80x is clouds
            (id.toInt() > 800) -> R.color.colorCloudy
            else -> R.color.colorSunny
        }
    }

}