package com.bonface.openweather.converters

import androidx.room.TypeConverter
import com.bonface.openweather.domain.model.DailyWeather
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    @ToJson
    fun fromDailyWeatherList(value: List<DailyWeather>?): String? {
        val type = Types.newParameterizedType(List::class.java, DailyWeather::class.java)
        val adapter = moshi.adapter<List<DailyWeather>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    @FromJson
    fun toDailyWeatherList(value: String?): List<DailyWeather>? {
        val type = Types.newParameterizedType(List::class.java, DailyWeather::class.java)
        val adapter = moshi.adapter<List<DailyWeather>>(type)
        return adapter.fromJson(value.toString())
    }


}