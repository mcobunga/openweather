package com.bonface.openweather.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp") val temp: Double?,
    @Json(name = "feels_like") val feelsLike: Double?,
    @Json(name = "temp_min") var tempMin: Double?,
    @Json(name = "temp_max") var tempMax: Double?,
    @Json(name = "pressure") val pressure: Double?,
    @Json(name = "sea_level") val seaLevel: Double?,
    @Json(name = "grnd_level") val grndLevel: Double?,
    @Json(name = "humidity") val humidity: Long?,
    @Json(name = "temp_kf") val tempKf: Double?,

) : Parcelable {
    fun getTemperature(): String {
        return "$temp°"
    }

    fun getMinTemperature(): String {
        return "$tempMin°"
    }

    fun getMaxTemperature(): String {
        return "$tempMax°"
    }
}
