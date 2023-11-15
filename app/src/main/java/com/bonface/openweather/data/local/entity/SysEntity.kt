package com.bonface.openweather.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.bonface.openweather.data.model.Sys
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "sys")
data class SysEntity(
    @ColumnInfo(name = "type") val type: Long?,
    @ColumnInfo(name = "sys_id") val sysId: Long?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "sunrise") val sunrise: Long?,
    @ColumnInfo(name = "sunset") val sunset: Long?,
    @ColumnInfo(name = "pod") val pod: String?

) : Parcelable {
    @Ignore
    constructor(sys: Sys?) : this(
        type = sys?.type,
        sysId = sys?.id,
        country = sys?.country,
        sunrise = sys?.sunrise,
        sunset = sys?.sunset,
        pod = sys?.pod
    )
}
