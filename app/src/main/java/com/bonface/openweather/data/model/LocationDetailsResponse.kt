package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class LocationDetailsResponse (
    @Json(name = "weather_id") val weatherId: Int?
): Parcelable