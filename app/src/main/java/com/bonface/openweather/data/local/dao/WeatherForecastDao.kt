package com.bonface.openweather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bonface.openweather.data.local.TABLE_WEATHER_FORECAST
import com.bonface.openweather.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherForecastDao {

    @Query("SELECT * FROM $TABLE_WEATHER_FORECAST")
    fun getWeatherWeather(): Flow<List<ForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeatherWeather(forecast: ForecastEntity)

    @Query("DELETE FROM $TABLE_WEATHER_FORECAST where latitude LIKE :location")
    suspend fun deleteWeatherForecast(location: String)

    @Query("DELETE FROM $TABLE_WEATHER_FORECAST")
    suspend fun deleteAllForecast()

    @Query("SELECT COUNT(*) FROM $TABLE_WEATHER_FORECAST")
    fun getCount(): Int

}