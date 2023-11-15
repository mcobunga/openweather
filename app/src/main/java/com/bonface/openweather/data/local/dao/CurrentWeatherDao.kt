package com.bonface.openweather.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bonface.openweather.data.local.TABLE_CURRENT_WEATHER
import com.bonface.openweather.data.model.CurrentWeather

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM $TABLE_CURRENT_WEATHER")
    fun getCurrentWeather(): LiveData<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(currentWeatherEntity: CurrentWeather)

    @Query("DELETE FROM $TABLE_CURRENT_WEATHER")
    fun deleteCurrentWeather()

}