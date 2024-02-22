package com.bonface.openweather.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class LocationDetailsResponse (
    @Json(name = "weather_id") val weatherId: Int?
): Parcelable