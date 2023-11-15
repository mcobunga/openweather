package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "type") val type: Long?,
    @Json(name = "id") val id: Long?,
    @Json(name = "country") val country: String?,
    @Json(name = "sunrise") val sunrise: Long?,
    @Json(name = "sunset") val sunset: Long?,
    @Json(name = "pod") val pod: String?
) : Parcelable
