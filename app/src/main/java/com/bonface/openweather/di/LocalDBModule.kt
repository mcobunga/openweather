package com.bonface.openweather.di

import android.content.Context
import androidx.room.Room
import com.bonface.openweather.data.local.OpenWeatherDatabase
import com.bonface.openweather.data.local.dao.CurrentWeatherDao
import com.bonface.openweather.data.local.dao.WeatherForecastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): OpenWeatherDatabase =
        Room.databaseBuilder(
            context,
            OpenWeatherDatabase::class.java,
            "weather.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideCurrentWeatherDao(weatherDb: OpenWeatherDatabase): CurrentWeatherDao = weatherDb.currentWeatherDao()

    @Provides
    @Singleton
    fun provideWeatherForecastDao(weatherDb: OpenWeatherDatabase): WeatherForecastDao = weatherDb.weatherForecastDao()


}