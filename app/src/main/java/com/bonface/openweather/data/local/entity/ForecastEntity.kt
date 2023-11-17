package com.bonface.openweather.data.local.entity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bonface.openweather.R
import com.bonface.openweather.data.local.TABLE_WEATHER_FORECAST
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@Parcelize
@Entity(tableName = TABLE_WEATHER_FORECAST)
data class ForecastEntity(
    @PrimaryKey(autoGenerate = false)@ColumnInfo(name = "day_of_week") val dayOfWeek: Long?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "max_temp") val maxTemp: Double?,
    @ColumnInfo(name = "min_temp") val minTemp: Double?,
    @ColumnInfo(name="eve_temp") val eveTemp: Double?,
    @ColumnInfo(name="night_temp") val nightTemp: Double?,
    @ColumnInfo(name="day_temp") val dayTemp: Double?,
    @ColumnInfo(name = "weather_id") val weatherId: Int?,
    @ColumnInfo(name = "weather_main") val weatherMain: String?,
    @ColumnInfo(name = "weather_desc") val weatherDesc: String?
): Parcelable {

    fun getDay(): String? {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return dayOfWeek.let { dt ->
            dt?.let { sdf.format(Date(it * 1000)) }
        }
    }

    fun getTemperature(): String {
        return "$dayTemp°"
    }

    fun getForecastWeatherIcon(): Int {
        return forecastWeatherIcon(weatherId.toString())
    }

    fun getMinTemperature(): String {
        return "$minTemp°"
    }

    fun getMaxTemperature(): String {
        return "$maxTemp°"
    }

    private fun forecastWeatherIcon(id: String): Int {
        return when {
            // id prefix 2xx is thunderstorm
            id.startsWith("2", true) -> R.drawable.rain
            //id prefix 3xx is drizzle
            id.startsWith("3", true) -> R.drawable.rain
            //id prefix 5xx is rain
            id.startsWith("5", true) -> R.drawable.rain
            //id prefix 6xx is snow
            id.startsWith("6", true) -> R.drawable.rain
            //id prefix 7xx is atmosphere
            id.startsWith("7", true) -> R.drawable.partlysunny
            //id 800 is clear
            (id == "800") -> R.drawable.clear
            //id prefix 80x is clouds
            (id.toInt() > 800) -> R.drawable.ic_baseline_cloud_queue_24
            else -> R.drawable.ic_baseline_cloud_queue_24
        }
    }

}