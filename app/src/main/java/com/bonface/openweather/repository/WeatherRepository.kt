package com.bonface.openweather.repository

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
    @NetworkModule.OpenWeatherApiKey private val apiKey: String,
) {

    suspend fun getCurrentWeatherByLocation(latitude: String, longitude: String): Flow<Response<CurrentWeather>> {
        return flow {
            emit(
                openWeatherApi.getCurrentWeatherByLocation(
                    latitude,
                    longitude,
                    units = Constants.UNITS,
                    appId = apiKey
                )
            )
        }.flowOn(Dispatchers.IO)

    }

    suspend fun getWeatherForecastByLocation(latitude: String, longitude: String): Flow<Response<WeatherForecast>> {
        return flow {
            emit(
                openWeatherApi.getWeatherForecastByLocation(
                    latitude,
                    longitude,
                    units = Constants.UNITS,
                    appId = apiKey,
                    exclude = Constants.EXCLUDE
                )
            )
        }.flowOn(Dispatchers.IO)
    }


}