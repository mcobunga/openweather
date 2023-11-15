package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed") val speed: Double?,
    @Json(name = "deg") val deg: Double?,
    @Json(name = "gust") val gust: Double?
) : Parcelable
