package com.bonface.openweather.data.remote

import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByLocation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): Response<CurrentWeather>

    @GET("data/2.5/onecall")
    suspend fun getWeatherForecastByLocation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String,
        @Query("appid") appId: String,
        @Query("exclude") exclude: String
    ): Response<WeatherForecast>


}