package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bonface.openweather.data.model.FavoriteLocations
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_locations")
data class FavoriteLocationsEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "latitude") val latitude: Double?,
    @ColumnInfo(name = "longitude") val longitude: Double?,
    @ColumnInfo(name = "date") val date: Long?
) : Parcelable {
    @Ignore
    constructor(favorite: FavoriteLocations?) : this(
        id = favorite?.id,
        name = favorite?.name,
        country = favorite?.country,
        latitude = favorite?.latitude,
        longitude = favorite?.longitude,
        date = favorite?.dt
    )
}
