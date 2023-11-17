package com.bonface.openweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.CurrentWeatherDao
import com.bonface.openweather.data.local.dao.WeatherForecastDao
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.ForecastEntity

const val TABLE_FAVORITE_PLACES = "favorite_places"
const val TABLE_CURRENT_WEATHER = "current_weather"
const val TABLE_WEATHER_FORECAST = "weather_forecast"

@Database(
    entities = [
        CurrentWeatherEntity::class,
        ForecastEntity::class,
        FavoritePlacesEntity::class
    ],
    version = 5,
    exportSchema = false
)

abstract class OpenWeatherDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherForecastDao(): WeatherForecastDao
    abstract fun favoritePlacesDao(): FavoritePlacesDao

    companion object {
        private var instance: OpenWeatherDatabase? = null

        fun getInstance(context: Context): OpenWeatherDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    OpenWeatherDatabase::class.java, "openWeatherDatabase.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }


}