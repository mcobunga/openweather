package com.bonface.openweather.data.local.entity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.*
import com.bonface.openweather.R
import com.bonface.openweather.data.local.TABLE_CURRENT_WEATHER
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.Weather
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@Parcelize
@Entity(tableName = TABLE_CURRENT_WEATHER)
data class CurrentWeatherEntity(
    @PrimaryKey(autoGenerate = false) @Embedded var coord: CoordEntity?,
    @ColumnInfo(name = "weather") val weather: List<Weather>?,
    @ColumnInfo(name = "base") val base: String?,
    @Embedded var main: MainEntity?,
    @ColumnInfo(name = "visibility") val visibility: Long?,
    @Embedded var wind: WindEntity?,
    @Embedded var clouds: CloudsEntity?,
    @ColumnInfo(name = "dt") val dt: Long?,
    @Embedded var sys: SysEntity?,
    @ColumnInfo(name = "timezone") val timezone: Long?,
    @ColumnInfo(name = "id") val id: Long?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "cod") val cod: Long?
) : Parcelable {
    @Ignore
    constructor(currentWeather: CurrentWeather) : this(
        coord = CoordEntity(currentWeather.coord),
        weather = currentWeather.weather,
        base = currentWeather.base,
        main = MainEntity(currentWeather.main),
        visibility = currentWeather.visibility,
        wind = WindEntity(currentWeather.wind),
        clouds = CloudsEntity(currentWeather.clouds),
        dt = currentWeather.dt,
        sys = SysEntity(currentWeather.sys),
        timezone = currentWeather.timezone,
        id = currentWeather.id,
        name = currentWeather.name,
        cod = currentWeather.cod
    )

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
        return dt.let { it?.let { it1 -> getDateTime(it1)?.getDisplayName(TextStyle.FULL, Locale.ENGLISH) } }
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
