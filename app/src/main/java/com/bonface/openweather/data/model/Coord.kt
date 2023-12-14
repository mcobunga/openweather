package com.bonface.openweather.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Coord(
    @Json(name = "lon") val lon: Double?,
    @Json(name = "lat") val lat: Double?
) : Parcelable
