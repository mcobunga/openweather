package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bonface.openweather.R
import com.bonface.openweather.data.local.TABLE_FAVORITE_PLACES
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = TABLE_FAVORITE_PLACES)
@Parcelize
data class FavoritePlacesEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name="id") val id: Long,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "temp") val temp: Double?,
    @ColumnInfo(name = "max_temp") val maxTemp: Double?,
    @ColumnInfo(name = "min_temp") val minTemp: Double?,
    @ColumnInfo(name = "weather_id") val weatherId: Int?,
    @ColumnInfo(name = "weather_main") val weatherMain: String?,
    @ColumnInfo(name = "weather_desc") val weatherDesc: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "current_loc") var isCurrentLocation: Boolean? = false,
    @ColumnInfo(name = "last_updated_at") val lastUpdatedAt: Long,
) : Parcelable