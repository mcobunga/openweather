package com.bonface.openweather.di

import com.bonface.openweather.domain.usecases.WeatherUseCase
import com.bonface.openweather.domain.usecases.WeatherUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideWeatherUseCase(
        useCase: WeatherUseCaseImpl
    ): WeatherUseCase
}