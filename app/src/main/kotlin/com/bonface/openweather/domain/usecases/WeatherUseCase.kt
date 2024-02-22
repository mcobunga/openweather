package com.bonface.openweather.domain.usecases

import android.location.Location
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.WeatherEntity
import com.bonface.openweather.utils.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherUseCase {

    suspend operator fun invoke(location: Location): Flow<Resource<WeatherEntity>>
    fun getWeather(): Flow<List<WeatherEntity>>
    suspend fun saveWeather(weatherEntity: WeatherEntity)
    suspend fun deleteWeather()
    fun getFavoritePlaces(): Flow<List<FavoritePlacesEntity>>
    suspend fun saveFavoritePlace(favoritePlacesEntity: FavoritePlacesEntity)
    fun isLocationAlreadyExists(latitude: Double, longitude: Double): Boolean


}