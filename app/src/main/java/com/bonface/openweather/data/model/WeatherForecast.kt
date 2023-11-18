package com.bonface.openweather.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class WeatherForecast(
	@Json(name="timezone") val timezone: String? = null,
	@Json(name="timezone_offset") val timezoneOffset: Int? = null,
	@Json(name="daily") val daily: List<DailyForecast>? = null,
	@Json(name="lon") val lon: Double? = null,
	@Json(name="lat") val lat: Double? = null
) : Parcelable