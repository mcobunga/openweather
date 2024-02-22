package com.bonface.openweather.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class DailyWeather(
    @Json(name = "day_of_week") val dayOfWeek: Long?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "max_temp") val maxTemp: Double?,
    @Json(name = "min_temp") val minTemp: Double?,
    @Json(name="eve_temp") val eveTemp: Double?,
    @Json(name="night_temp") val nightTemp: Double?,
    @Json(name="day_temp") val dayTemp: Double?,
    @Json(name = "weather_id") val weatherId: Int?,
    @Json(name = "weather_main") val weatherMain: String?,
    @Json(name = "weather_desc") val weatherDesc: String?
): Parcelable