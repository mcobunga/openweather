package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.*
import com.bonface.openweather.data.local.TABLE_FORECAST_WEATHER
import com.bonface.openweather.data.model.DailyForecast
import com.bonface.openweather.data.model.WeatherForecast
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_FORECAST_WEATHER)
data class WeatherForecastEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "timezone") var timezone: String?,
    @ColumnInfo(name = "timezone_offset") var timezoneOffset: Int?,
    @ColumnInfo(name = "daily") var daily: List<DailyForecast>? = null,
    @ColumnInfo(name = "lon") var lon: Double?,
    @ColumnInfo(name = "lat") var lat: Double?
) : Parcelable {
    @Ignore
    constructor(forecast: WeatherForecast): this(
        timezone = forecast.timezone,
        timezoneOffset = forecast.timezoneOffset,
        daily = forecast.daily,
        lon = forecast.lon,
        lat = forecast.lat
    )
}
