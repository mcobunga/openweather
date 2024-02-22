package com.bonface.openweather.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
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
) : Parcelable