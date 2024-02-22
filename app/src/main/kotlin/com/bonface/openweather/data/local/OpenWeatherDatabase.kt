package com.bonface.openweather.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bonface.openweather.converters.Converters
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.WeatherDao
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.WeatherEntity

const val TABLE_FAVORITE_PLACES = "favorite_places"
const val TABLE_WEATHER = "weather"

@Database(
    entities = [
        WeatherEntity::class,
        FavoritePlacesEntity::class
    ],
    version = 6,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class OpenWeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun favoritePlacesDao(): FavoritePlacesDao

    companion object {
        private var instance: OpenWeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context,
            OpenWeatherDatabase::class.java,
            "openWeatherDatabase_db.db",
        ).fallbackToDestructiveMigration().build()

        fun destroyInstance() {
            instance = null
        }
    }

}