package com.bonface.openweather.data.remote

import com.bonface.openweather.domain.model.CurrentWeather
import com.bonface.openweather.domain.model.WeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentLocationWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String
    ): Response<CurrentWeather>

    @GET("data/2.5/onecall")
    suspend fun getCurrentLocationWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("exclude") exclude: String
    ): Response<WeatherForecast>

}