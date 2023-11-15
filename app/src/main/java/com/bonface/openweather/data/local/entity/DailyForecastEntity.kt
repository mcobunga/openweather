package com.bonface.openweather.data.local.entity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bonface.openweather.R
import com.bonface.openweather.data.model.FeelsLike
import com.bonface.openweather.data.model.Temp
import com.bonface.openweather.data.model.Weather
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@Entity(tableName = "daily_forecast")
@Parcelize
data class DailyForecastEntity(
    @ColumnInfo(name="moonset") val moonset: Int? = null,
    @ColumnInfo(name="rain") val rain: Double? = null,
    @ColumnInfo(name="sunrise") val sunrise: Int? = null,
    @ColumnInfo(name="temp") val temp: Temp? = null,
    @ColumnInfo(name="moon_phase") val moonPhase: Double? = null,
    @ColumnInfo(name="uvi") val uvi: Double? = null,
    @ColumnInfo(name="moonrise") val moonrise: Int? = null,
    @ColumnInfo(name="pressure") val pressure: Int? = null,
    @ColumnInfo(name="clouds") val clouds: Int? = null,
    @ColumnInfo(name="feels_like") val feelsLike: FeelsLike? = null,
    @ColumnInfo(name="wind_gust") val windGust: Double? = null,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name="dt") val dt: Long? = null,
    @ColumnInfo(name="pop") val pop: Double? = null,
    @ColumnInfo(name="wind_deg") val windDeg: Int? = null,
    @ColumnInfo(name="dew_point") val dewPoint: Double? = null,
    @ColumnInfo(name="sunset") val sunset: Int? = null,
    @ColumnInfo(name="weather") val weather: List<Weather?>? = null,
    @ColumnInfo(name="humidity") val humidity: Int? = null,
    @ColumnInfo(name="wind_speed") val windSpeed: Double? = null
) : Parcelable {

    fun getForecastWeatherIcon(): Int {
        return forecastWeatherIcon(weather?.firstOrNull()?.id.toString())
    }

    private fun forecastWeatherIcon(id: String): Int {
        return when (id) {
            // id prefix 2xx is thunderstorm
            id.startsWith("2", true).toString() -> R.drawable.rain
            //id prefix 3xx is drizzle
            id.startsWith("3", true).toString() -> R.drawable.rain
            //id prefix 5xx is rain
            id.startsWith("5", true).toString() -> R.drawable.rain
            //id prefix 6xx is snow
            id.startsWith("6", true).toString() -> R.drawable.rain
            //id prefix 7xx is atmosphere
            id.startsWith("7", true).toString() -> R.drawable.partlysunny
            //id 800 is clear
            (id == "800").toString() -> R.drawable.clear
            //id prefix 80x is clouds
            (id.toInt() > 800).toString() -> R.drawable.ic_baseline_cloud_queue_24
            else -> R.drawable.ic_baseline_cloud_queue_24
        }
    }

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
