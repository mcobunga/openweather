package com.bonface.openweather.data.local.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.bonface.openweather.data.local.TABLE_WEATHER_FORECAST
import com.bonface.openweather.data.local.entity.WeatherForeCastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherForecastDao {

    @Query("SELECT * FROM $TABLE_WEATHER_FORECAST")
    fun getForecastList(): Flow<List<WeatherForeCastEntity>>

    @Insert(onConflict = REPLACE)
    fun insertWeatherForecast(forecast: WeatherForeCastEntity)

    @Query("DELETE FROM $TABLE_WEATHER_FORECAST where location LIKE :location")
    suspend fun deleteWeatherForecast(location: String)

    @Query("DELETE FROM $TABLE_WEATHER_FORECAST")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM $TABLE_WEATHER_FORECAST")
    fun getCount(): Int


}