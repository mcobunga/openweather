package com.bonface.openweather.di

import android.content.Context
import com.bonface.openweather.data.local.OpenWeatherDatabase
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.WeatherDao
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
    fun provideDatabase(@ApplicationContext context: Context): OpenWeatherDatabase = OpenWeatherDatabase.invoke(context)


    @Provides
    @Singleton
    fun provideWeatherDao(weatherDb: OpenWeatherDatabase): WeatherDao = weatherDb.weatherDao()

    @Provides
    @Singleton
    fun provideFavoritePlacesDao(weatherDb: OpenWeatherDatabase): FavoritePlacesDao = weatherDb.favoritePlacesDao()


}