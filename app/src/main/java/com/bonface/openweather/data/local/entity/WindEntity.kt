package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.bonface.openweather.data.model.Wind
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "wind")
data class WindEntity(
    @ColumnInfo(name = "deg") val deg: Double?,
    @ColumnInfo(name = "speed") val speed: Double?
) : Parcelable {
    @Ignore
    constructor(wind: Wind?) : this(
        deg = wind?.deg,
        speed = wind?.speed
    )
}
