package com.bonface.openweather.repository

import android.location.Location
import com.bonface.openweather.data.local.dao.CurrentWeatherDao
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.WeatherForecastDao
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.ForecastEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.data.remote.OpenWeatherApi
import com.bonface.openweather.di.NetworkModule
import com.bonface.openweather.utils.Constants
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherForecastDao: WeatherForecastDao,
    private val favoriteLocationDao: FavoritePlacesDao,
    @NetworkModule.OpenWeatherApiKey private val apiKey: String,
) {

    suspend fun getCurrentLocationWeather(location: Location): Response<CurrentWeather> {
        return openWeatherApi.getCurrentLocationWeather(
            location.latitude,
            location.longitude,
            units = Constants.UNITS,
            appId = apiKey
        )

    }

    suspend fun getCurrentLocationWeatherForecast(location: Location): Response<WeatherForecast> {
        return openWeatherApi.getCurrentLocationWeatherForecast(
            location.latitude,
            location.longitude,
            units = Constants.UNITS,
            appId = apiKey,
            exclude = Constants.EXCLUDE
        )
    }

    fun getCurrentWeather() = currentWeatherDao.getCurrentWeather()

    suspend fun saveCurrentWeather(currentWeatherEntity: CurrentWeatherEntity) = currentWeatherDao.saveCurrentWeather(currentWeatherEntity)

    suspend fun deleteCurrentWeather() = currentWeatherDao.deleteAllCurrent()

    fun getWeatherForecast() = weatherForecastDao.getWeatherWeather()

    suspend fun saveWeatherForecast(forecastEntity: ForecastEntity) = weatherForecastDao.saveWeatherWeather(forecastEntity)

    suspend fun deleteWeatherForecast() = weatherForecastDao.deleteAllForecast()

    fun getFavoritePlaces() = favoriteLocationDao.getFavoritePlaces()

    suspend fun saveFavoritePlace(favoritePlacesEntity: FavoritePlacesEntity) = favoriteLocationDao.saveFavoritePlace(favoritePlacesEntity)

    fun isLocationAlreadyExists(latitude: Double, longitude: Double) = favoriteLocationDao.isLocationAlreadyExists(latitude, longitude)


}