package com.bonface.openweather.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun providesFusedLocationProvider(@ApplicationContext context: Context) = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @Singleton
    fun providesGeoCoder(@ApplicationContext context: Context) = Geocoder(context)

}