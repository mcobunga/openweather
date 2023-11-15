package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.bonface.openweather.data.model.Clouds
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "clouds")
data class CloudsEntity(
    @ColumnInfo(name = "all") var all: Long?
) : Parcelable {
    @Ignore
    constructor(clouds: Clouds?) : this(
        all = clouds?.all
    )
}
