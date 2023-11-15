package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String?,
    @Json(name = "coord") val coord: Coord?,
    @Json(name = "country") val country: String?,
    @Json(name = "population") val population: String?,
    @Json(name = "timezone") val timezone: Long?,
    @Json(name = "sunrise") val sunrise: Long?,
    @Json(name = "sunset") val sunset: Long?
) : Parcelable
