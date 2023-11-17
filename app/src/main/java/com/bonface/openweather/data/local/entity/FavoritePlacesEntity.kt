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
) : Parcelable {
    fun lastUpdated(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return run {
            sdf.format(Date(lastUpdatedAt))
        }
    }

    fun getTemperature(): String {
        return "$temp°"
    }

    fun getMinTemperature(): String {
        return "$minTemp°"
    }

    fun getMaxTemperature(): String {
        return "$maxTemp°"
    }

    fun getCurrentWeatherImage(): Int {
        return currentWeatherImage(weatherId.toString())
    }

    private fun currentWeatherImage(id: String): Int {
        return when {
            // id prefix 2xx is thunderstorm
            id.startsWith("2", true) -> R.drawable.forest_rainy
            //id prefix 3xx is drizzle
            id.startsWith("3", true) -> R.drawable.forest_rainy
            //id prefix 5xx is rain
            id.startsWith("5", true) -> R.drawable.forest_rainy
            //id prefix 6xx is snow
            id.startsWith("6", true) -> R.drawable.forest_rainy
            //id prefix 7xx is atmosphere
            id.startsWith("7", true) -> R.drawable.forest_sunny
            //id 800 is clear
            (id == "800") -> R.drawable.forest_cloudy
            //id prefix 80x is clouds
            (id.toInt() > 800) -> R.drawable.forest_cloudy
            else -> R.drawable.forest_sunny
        }
    }

    fun getCurrentWeatherBackgroundColor(): Int {
        return currentWeatherBackgroundColor(weatherId.toString())
    }

    private fun currentWeatherBackgroundColor(id: String): Int {
        return when {
            // id prefix 2xx is thunderstorm
            id.startsWith("2", true) -> R.color.colorRainy
            //id prefix 3xx is drizzle
            id.startsWith("3", true) -> R.color.colorRainy
            //id prefix 5xx is rain
            id.startsWith("5", true) -> R.color.colorRainy
            //id prefix 6xx is snow
            id.startsWith("6", true) -> R.color.colorRainy
            //id prefix 7xx is atmosphere
            id.startsWith("7", true) -> R.color.colorSunny
            //id 800 is clear
            (id == "800") -> R.color.colorCloudy
            //id prefix 80x is clouds
            (id.toInt() > 800) -> R.color.colorCloudy
            else -> R.color.colorSunny
        }
    }
}