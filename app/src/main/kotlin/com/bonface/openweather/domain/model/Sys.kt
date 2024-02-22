package com.bonface.openweather.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
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
