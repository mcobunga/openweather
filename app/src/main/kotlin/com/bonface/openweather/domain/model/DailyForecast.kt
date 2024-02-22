package com.bonface.openweather.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@JsonClass(generateAdapter = true)
@Parcelize
data class DailyForecast(
    @Json(name="moonset") val moonset: Int? = null,
    @Json(name="rain") val rain: Double? = null,
    @Json(name="sunrise") val sunrise: Int? = null,
    @Json(name="temp") val temp: Temp? = null,
    @Json(name="moon_phase") val moonPhase: Double? = null,
    @Json(name="uvi") val uvi: Double? = null,
    @Json(name="moonrise") val moonrise: Int? = null,
    @Json(name="pressure") val pressure: Int? = null,
    @Json(name="clouds") val clouds: Int? = null,
    @Json(name="feels_like") val feelsLike: FeelsLike? = null,
    @Json(name="wind_gust") val windGust: Double? = null,
    @Json(name="dt") val dt: Long? = null,
    @Json(name="pop") val pop: Double? = null,
    @Json(name="wind_deg") val windDeg: Int? = null,
    @Json(name="dew_point") val dewPoint: Double? = null,
    @Json(name="sunset") val sunset: Int? = null,
    @Json(name="weather") val weather: List<Weather>? = null,
    @Json(name="humidity") val humidity: Int? = null,
    @Json(name="wind_speed") val windSpeed: Double? = null
) : Parcelable