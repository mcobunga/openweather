package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "id") val id: Long?,
    @Json(name = "main") val main: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "icon") val icon: String?
) : Parcelable {
    fun getWeatherDescription(): String {
        return description?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            .toString()
    }
}
