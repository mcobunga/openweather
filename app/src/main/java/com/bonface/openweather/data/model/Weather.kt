package com.bonface.openweather.data.model

import android.os.Parcelable
import com.bonface.openweather.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "id") val id: Int?,
    @Json(name = "main") val main: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "icon") val icon: String?
) : Parcelable {
    fun getForecastWeatherIcon(): Int {
        return forecastWeatherIcon(id.toString())
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
}
