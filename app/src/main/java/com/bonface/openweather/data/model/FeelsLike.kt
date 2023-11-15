package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class FeelsLike(
    @Json(name="eve") val eve: Double? = null,
    @Json(name="night") val night: Double? = null,
    @Json(name="day") val day: Double? = null,
    @Json(name="morn") val morn: Double? = null
) : Parcelable