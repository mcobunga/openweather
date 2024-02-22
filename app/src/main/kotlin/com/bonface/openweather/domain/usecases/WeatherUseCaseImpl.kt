package com.bonface.openweather.domain.usecases

import android.location.Location
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.WeatherEntity
import com.bonface.openweather.domain.model.CurrentWeather
import com.bonface.openweather.domain.model.WeatherForecast
import com.bonface.openweather.domain.repository.WeatherRepository
import com.bonface.openweather.mappers.toWeatherEntity
import com.bonface.openweather.utils.ErrorHandler
import com.bonface.openweather.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import retrofit2.Response
import javax.inject.Inject

class WeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
    private val errorHandler: ErrorHandler
): WeatherUseCase {

    override suspend fun invoke(location: Location) = flow {
        try {
            emit(Resource.Loading())
            flowOf(repository.getCurrentLocationWeather(location))
                .zip(flowOf(repository.getCurrentLocationWeatherForecast(location))) { current, forecast ->
                    return@zip Pair<Response<CurrentWeather>, Response<WeatherForecast>>(
                        current,
                        forecast
                    )
                }.collect {
                    emit(handleResponse(it))
                }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(errorHandler.handleException(e).message.toString()))
        }
    }

    override fun getWeather(): Flow<List<WeatherEntity>> = repository.getWeather()

    override suspend fun saveWeather(weatherEntity: WeatherEntity) = repository.saveWeather(weatherEntity)

    override suspend fun deleteWeather() = repository.deleteWeather()

    override fun getFavoritePlaces(): Flow<List<FavoritePlacesEntity>> = repository.getFavoritePlaces()

    override suspend fun saveFavoritePlace(favoritePlacesEntity: FavoritePlacesEntity) = repository.saveFavoritePlace(favoritePlacesEntity)

    override fun isLocationAlreadyExists(latitude: Double, longitude: Double): Boolean = repository.isLocationAlreadyExists(latitude, longitude)


    private  fun handleResponse(response: Pair<Response<CurrentWeather>, Response<WeatherForecast>>): Resource<WeatherEntity> {
        if (response.first.isSuccessful && response.first.body() != null) {
            val current = response.first.body()
            if (response.second.isSuccessful && response.second.body() != null) {
                val forecast = response.second.body()
                val pairedResult = Pair(current!!, forecast!!)
                return Resource.Success(pairedResult.toWeatherEntity())
            }
            return Resource.Error(response.second.message())
        }
        return Resource.Error(response.first.message())
    }

}