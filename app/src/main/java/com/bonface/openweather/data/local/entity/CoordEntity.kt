package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.bonface.openweather.data.model.Coord
import com.bonface.openweather.data.model.Geoloc
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "coord")
data class CoordEntity(
    @ColumnInfo(name = "lon") val lon: Double?,
    @ColumnInfo(name = "lat") val lat: Double?
) : Parcelable {
    @Ignore
    constructor(coord: Coord?) : this(
        lon = coord?.lon,
        lat = coord?.lat
    )

    @Ignore
    constructor(geoloc: Geoloc?) : this(
        lon = geoloc?.lng,
        lat = geoloc?.lat
    )
}
