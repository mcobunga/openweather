package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Temp(
    @Json(name="min") val min: Double? = null,
    @Json(name="max") val max: Double? = null,
    @Json(name="eve") val eve: Double? = null,
    @Json(name="night") val night: Double? = null,
    @Json(name="day") val day: Double? = null,
    @Json(name="morn") val morn: Double? = null
) : Parcelable