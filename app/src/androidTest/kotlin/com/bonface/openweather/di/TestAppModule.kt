package com.bonface.openweather.di

import android.content.Context
import androidx.room.Room
import com.bonface.openweather.data.local.OpenWeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_database")
    fun provideInMemoryRoomDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context,
            OpenWeatherDatabase::class.java
        ).allowMainThreadQueries().build()

}