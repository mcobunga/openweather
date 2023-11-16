package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bonface.openweather.data.local.TABLE_FAVORITE_PLACES
import com.bonface.openweather.data.local.TABLE_WEATHER_FORECAST
import kotlinx.parcelize.Parcelize

@Entity(tableName = TABLE_FAVORITE_PLACES)
@Parcelize
data class FavoritePlacesEntity(
    @ColumnInfo(name = "location") val location: String?,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "temperature") val temperature: Double?,
    @ColumnInfo(name = "max_temp") val maxTemp: Double?,
    @ColumnInfo(name = "min_temp") val minTemp: Double?,
    @ColumnInfo(name = "weather_id") val weatherId: Int?,
    @ColumnInfo(name = "weather_main") val weatherMain: String?,
    @ColumnInfo(name = "weather_desc") val weatherDesc: String?,
    @ColumnInfo(name = "is_current") val isCurrentUserLocation: Boolean? = false,
    @ColumnInfo(name = "last_updated_at") val lastUpdatedAt: Long?,
) : Parcelable


@Entity(tableName = TABLE_WEATHER_FORECAST)
data class WeatherForeCastEntity(
    @ColumnInfo(name = "day_of_week") val day: Long?,
    @ColumnInfo(name = "location") val location: String?,
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "temperature") val temperature: Double?,
    @ColumnInfo(name = "max_temp") val maxTemp: Double?,
    @ColumnInfo(name = "min_temp") val minTemp: Double?,
    @ColumnInfo(name = "weather_id") val weatherId: Int?,
    @ColumnInfo(name = "weather_main") val weatherMain: String?,
    @ColumnInfo(name = "weather_desc") val weatherDesc: String?,
    @ColumnInfo(name = "last_updated_at") val lastUpdatedAt: Long?
)