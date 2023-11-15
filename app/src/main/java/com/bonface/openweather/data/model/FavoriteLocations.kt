package com.bonface.openweather.data.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

@Parcelize
@JsonClass(generateAdapter = true)
data class FavoriteLocations(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "date") val dt: Long?
): Parcelable {
    fun getLocationName(): String{
        return "$name, $country"
    }

    fun dateSaved(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        return if (dt != null){
            val netDate = Date(dt * 1000)
            "Saved on: "+ sdf.format(netDate)
        }else ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTime(s: Long): DayOfWeek? {
        return try {
            val newDate = Date(s * 1000)
            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH::MM:ss", Locale.ENGLISH).format(newDate)
            LocalDate.of(
                formattedDate.substringAfterLast("/").toInt(),
                formattedDate.substringAfter("/").take(2).toInt(),
                formattedDate.substringBefore("/").toInt()
            ).dayOfWeek
        } catch (e: Exception) {
            e.printStackTrace()
            DayOfWeek.MONDAY
        }
    }

}