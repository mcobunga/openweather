package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bonface.openweather.data.local.TABLE_WEATHER
import com.bonface.openweather.domain.model.DailyForecast
import com.bonface.openweather.domain.model.DailyWeather
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_WEATHER)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name="id") val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "dt") val dt: Long?,
    @ColumnInfo(name = "temp") val temp: Double?,
    @ColumnInfo(name = "max_temp") val maxTemp: Double?,
    @ColumnInfo(name = "min_temp") val minTemp: Double?,
    @ColumnInfo(name = "weather_id") val weatherId: Int?,
    @ColumnInfo(name = "weather_main") val weatherMain: String?,
    @ColumnInfo(name = "weather_desc") val weatherDesc: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "forecast") val forecast: List<DailyWeather>?,
    @ColumnInfo(name = "last_updated_at") val lastUpdatedAt: Long
): Parcelable