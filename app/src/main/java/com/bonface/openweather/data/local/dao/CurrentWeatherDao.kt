package com.bonface.openweather.data.local.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.bonface.openweather.data.local.TABLE_CURRENT_WEATHER
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM $TABLE_CURRENT_WEATHER")
    fun getCurrentWeather(): Flow<List<CurrentWeatherEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun saveCurrentWeather(forecast: CurrentWeatherEntity)

    @Query("DELETE FROM $TABLE_CURRENT_WEATHER where name LIKE :location")
    suspend fun deleteCurrentWeather(location: String)

    @Query("DELETE FROM $TABLE_CURRENT_WEATHER")
    suspend fun deleteAllCurrent()

    @Query("SELECT COUNT(*) FROM $TABLE_CURRENT_WEATHER")
    fun getCount(): Int

}