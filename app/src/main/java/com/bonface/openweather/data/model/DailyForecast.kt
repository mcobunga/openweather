package com.bonface.openweather.data.model

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.bonface.openweather.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@JsonClass(generateAdapter = true)
@Parcelize
data class DailyForecast(
    @Json(name="moonset") val moonset: Int? = null,
    @Json(name="rain") val rain: Double? = null,
    @Json(name="sunrise") val sunrise: Int? = null,
    @Json(name="temp") val temp: Temp? = null,
    @Json(name="moon_phase") val moonPhase: Double? = null,
    @Json(name="uvi") val uvi: Double? = null,
    @Json(name="moonrise") val moonrise: Int? = null,
    @Json(name="pressure") val pressure: Int? = null,
    @Json(name="clouds") val clouds: Int? = null,
    @Json(name="feels_like") val feelsLike: FeelsLike? = null,
    @Json(name="wind_gust") val windGust: Double? = null,
    @Json(name="dt") val dt: Long? = null,
    @Json(name="pop") val pop: Double? = null,
    @Json(name="wind_deg") val windDeg: Int? = null,
    @Json(name="dew_point") val dewPoint: Double? = null,
    @Json(name="sunset") val sunset: Int? = null,
    @Json(name="weather") val weather: List<Weather>? = null,
    @Json(name="humidity") val humidity: Int? = null,
    @Json(name="wind_speed") val windSpeed: Double? = null
) : Parcelable {

    @SuppressLint("NewApi")
    fun getDay(): String? {
        return dt.let { dt ->
            dt?.let { getDateTime(it)?.getDisplayName(TextStyle.FULL, Locale.ENGLISH) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTime(s: Long): DayOfWeek? {
        return try {
            val newDate = Date(s * 1000)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(newDate)
            LocalDate.of(
                formattedDate.substringAfterLast("/").toInt(),
                formattedDate.substringAfter("/").take(2).toInt(),
                formattedDate.substringBefore("/").toInt()
            ).dayOfWeek
        } catch (e: Exception) {
            e.printStackTrace()
            DayOfWeek.MONDAY
        }
    }

}
