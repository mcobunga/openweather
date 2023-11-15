package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import com.bonface.openweather.data.model.City
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "city")
data class CityEntity(
    @ColumnInfo(name = "city_id") var cityId: Long?,
    @ColumnInfo(name = "name") var name: String?,
    @Embedded var coord: CoordEntity?,
    @ColumnInfo(name = "country") var country: String?,
    @ColumnInfo(name = "population") var population: String?,
    @ColumnInfo(name = "timezone") var timezone: Long?,
    @ColumnInfo(name = "sunrise") var sunrise: Long?,
    @ColumnInfo(name = "sunset") var sunset: Long?
) : Parcelable {
    @Ignore
    constructor(city: City?) : this(
        cityId = city?.id,
        name = city?.name,
        coord = city?.coord?.let { CoordEntity(it) },
        country = city?.country,
        population = city?.population,
        timezone = city?.timezone,
        sunrise = city?.sunrise,
        sunset = city?.sunset
    )
}
