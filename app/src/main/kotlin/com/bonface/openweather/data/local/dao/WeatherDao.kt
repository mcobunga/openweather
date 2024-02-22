package com.bonface.openweather.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bonface.openweather.data.local.TABLE_WEATHER
import com.bonface.openweather.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM $TABLE_WEATHER")
    fun getWeather(): Flow<List<WeatherEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun saveWeather(weather: WeatherEntity)

    @Query("DELETE FROM $TABLE_WEATHER where name LIKE :location")
    suspend fun deleteWeatherInfoByLocationName(location: String)

    @Delete
    suspend fun deleteWeatherInfo(weather: WeatherEntity)

    @Query("DELETE FROM $TABLE_WEATHER")
    suspend fun nukeTable()

    @Query("SELECT COUNT(*) FROM $TABLE_WEATHER")
    fun getCount():  Flow<Int>

}