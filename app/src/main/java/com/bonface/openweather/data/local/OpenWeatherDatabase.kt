package com.bonface.openweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bonface.openweather.data.local.dao.Converters
import com.bonface.openweather.data.local.dao.CurrentWeatherDao
import com.bonface.openweather.data.local.dao.WeatherForecastDao
import com.bonface.openweather.data.local.entity.CityEntity
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.local.entity.WeatherForecastEntity

const val TABLE_CURRENT_WEATHER = "weather_current"
const val TABLE_FORECAST_WEATHER = "DetailedTransaction"
const val TABLE_FAVORITE_PLACES = "favorite_places"
const val TABLE_CITY = "city"

@Database(
    entities = [
        CurrentWeatherEntity::class,
        WeatherForecastEntity::class,
        CityEntity::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class OpenWeatherDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao

    abstract fun weatherForecastDao(): WeatherForecastDao

    //abstract fun favoriteLocationsDao(): FavoriteLocationsDao

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