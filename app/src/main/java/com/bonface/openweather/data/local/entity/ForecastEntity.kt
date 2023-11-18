package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bonface.openweather.data.local.TABLE_WEATHER_FORECAST
import kotlinx.parcelize.Parcelize

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
): Parcelable