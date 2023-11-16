package com.bonface.openweather.repository

import android.location.Location
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.WeatherForecastDao
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.WeatherForeCastEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.data.remote.OpenWeatherApi
import com.bonface.openweather.di.NetworkModule
import com.bonface.openweather.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val weatherForecastDao: WeatherForecastDao,
    private val favoriteLocationDao: FavoritePlacesDao,
    @NetworkModule.OpenWeatherApiKey private val apiKey: String,
) {

    suspend fun getCurrentWeatherByLocation(location: Location): Flow<Response<CurrentWeather>> {
        return flow {
            emit(
                openWeatherApi.getCurrentWeatherByLocation(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    units = Constants.UNITS,
                    appId = apiKey
                )
            )
        }.flowOn(Dispatchers.IO)

    }

    suspend fun getWeatherForecastByLocation(location: Location): Flow<Response<WeatherForecast>> {
        return flow {
            emit(
                openWeatherApi.getWeatherForecastByLocation(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    units = Constants.UNITS,
                    appId = apiKey,
                    exclude = Constants.EXCLUDE
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    suspend fun saveCurrentUserLocation(favoritePlacesEntity: FavoritePlacesEntity) =
        favoriteLocationDao.saveFavoritePlace(favoritePlacesEntity)

    fun getCurrentUserLocation() = favoriteLocationDao.getCurrentUserLocation()

    fun getAllFavoritePlaces() = favoriteLocationDao.getFavoritePlaces()

    suspend fun removeFavoritePlace(favoritePlacesEntity: FavoritePlacesEntity) = favoriteLocationDao.removeFavoritePlace(favoritePlacesEntity)

    suspend fun deleteAllPlaces() = favoriteLocationDao.deleteAll()

    suspend fun saveWeatherForecast(weatherForeCastEntity: WeatherForeCastEntity) = weatherForecastDao.insertWeatherForecast(weatherForeCastEntity)

    fun getAllWeatherForecast() = weatherForecastDao.getForecastList()

    suspend fun deleteAllWeatherForecast() = weatherForecastDao.deleteAll()

    suspend fun deleteWeatherForecast(location: String) = weatherForecastDao.deleteWeatherForecast(location)

}