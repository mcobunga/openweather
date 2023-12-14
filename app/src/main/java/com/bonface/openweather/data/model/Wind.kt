package com.bonface.openweather.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed") val speed: Double?,
    @Json(name = "deg") val deg: Double?,
    @Json(name = "gust") val gust: Double?
) : Parcelable
