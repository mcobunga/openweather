package com.bonface.openweather.domain.repository

import android.location.Location
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.WeatherDao
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.WeatherEntity
import com.bonface.openweather.data.remote.OpenWeatherApi
import com.bonface.openweather.domain.model.CurrentWeather
import com.bonface.openweather.domain.model.WeatherForecast
import com.bonface.openweather.utils.Constants.EXCLUDE
import com.bonface.openweather.utils.Constants.UNITS
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val weatherDao: WeatherDao,
    private val favoriteLocationDao: FavoritePlacesDao
) {

    suspend fun getCurrentLocationWeather(location: Location): Response<CurrentWeather> {
        return openWeatherApi.getCurrentLocationWeather(
            location.latitude,
            location.longitude,
            units = UNITS
        )
    }

    suspend fun getCurrentLocationWeatherForecast(location: Location): Response<WeatherForecast> {
        return openWeatherApi.getCurrentLocationWeatherForecast(
            location.latitude,
            location.longitude,
            units = UNITS,
            exclude = EXCLUDE
        )
    }

    fun getWeather() = weatherDao.getWeather()

    suspend fun saveWeather(weatherEntity: WeatherEntity) = weatherDao.saveWeather(weatherEntity)

    suspend fun deleteWeather() = weatherDao.nukeTable()

    fun getFavoritePlaces() = favoriteLocationDao.getFavoritePlaces()

    suspend fun saveFavoritePlace(favoritePlacesEntity: FavoritePlacesEntity) = favoriteLocationDao.saveFavoritePlace(favoritePlacesEntity)

    fun isLocationAlreadyExists(latitude: Double, longitude: Double) = favoriteLocationDao.isLocationAlreadyExists(latitude, longitude)


}