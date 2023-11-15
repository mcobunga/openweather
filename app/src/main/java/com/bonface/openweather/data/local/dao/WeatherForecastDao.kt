package com.bonface.openweather.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.bonface.openweather.data.local.TABLE_FORECAST_WEATHER
import com.bonface.openweather.data.model.WeatherForecast

@Dao
interface WeatherForecastDao {

    @Query("SELECT * FROM $TABLE_FORECAST_WEATHER")
    fun getForecastList(): LiveData<WeatherForecast>

    @Insert(onConflict = REPLACE)
    fun insertWeatherForecast(forecast: WeatherForecast)

    @Query("DELETE FROM $TABLE_FORECAST_WEATHER")
    suspend fun nukeTable()

    @Query("SELECT COUNT(*) FROM $TABLE_FORECAST_WEATHER")
    fun getCount(): Int
}