package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.bonface.openweather.data.model.Main
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "main")
data class MainEntity(
    @ColumnInfo(name = "temp") var temp: Double?,
    @ColumnInfo(name = "feels_like") var feelsLike: Double?,
    @ColumnInfo(name = "temp_min") var tempMin: Double?,
    @ColumnInfo(name = "temp_max") var tempMax: Double?,
    @ColumnInfo(name = "pressure") var pressure: Double?,
    @ColumnInfo(name = "humidity") var humidity: Long?
) : Parcelable {
    @Ignore
    constructor(main: Main?) : this(
        temp = main?.temp,
        feelsLike = main?.feelsLike,
        tempMin = main?.tempMin,
        tempMax = main?.tempMax,
        pressure = main?.pressure,
        humidity = main?.humidity
    )

    fun getTemperature(): String {
        return temp.toString() + "°"
    }

    fun getMinTemperature(): String {
        val min = "\nmin"
        return tempMin.toString() + "°" + min
    }

    fun getCurrentTemperature(): String {
        val current = "\ncurrent"
        return getTemperature() + current
    }

    fun getMaxTemperature(): String {
        val max = "\nmax"
        return tempMax.toString() + "°" + max
    }
}
