package com.bonface.openweather.utils

import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.ForecastEntity

object TestCreationUtils {

    fun getCurrentWeather(): CurrentWeatherEntity = CurrentWeatherEntity(
        id = 192126,
        name = "Kikuyu",
        latitude = -1.267,
        longitude = 36.695,
        dt = 1700286720,
        temp = 17.48,
        maxTemp = 17.53,
        minTemp = 15.81,
        weatherId = 802,
        weatherMain = "Clouds",
        weatherDesc = "scattered clouds",
        country = "KE",
        lastUpdatedAt = System.currentTimeMillis()
    )

    fun getWeatherForecast(): List<ForecastEntity> = listOf(
        ForecastEntity(
            dayOfWeek = 1700298000,
            latitude = -1.267,
            longitude = 36.695,
            maxTemp = 23.16,
            minTemp = 15.38,
            eveTemp = 19.65,
            nightTemp = 15.58,
            dayTemp = 19.86,
            weatherId = 500,
            weatherMain = "Rain",
            weatherDesc = "light rain"
        ),
        ForecastEntity(
            dayOfWeek = 1700384400,
            latitude = -1.267,
            longitude = 36.695,
            maxTemp = 22.3,
            minTemp = 15.22,
            eveTemp = 17.92,
            nightTemp = 15.49,
            dayTemp = 20.93,
            weatherId = 500,
            weatherMain = "Rain",
            weatherDesc = "light rain"
        ),
        ForecastEntity(
            dayOfWeek = 1700470800,
            latitude = -1.267,
            longitude = 36.695,
            maxTemp = 23.16,
            minTemp = 15.38,
            eveTemp = 19.65,
            nightTemp = 15.58,
            dayTemp = 19.86,
            weatherId = 501,
            weatherMain = "Rain",
            weatherDesc = "moderate rain"
        ),
        ForecastEntity(
            dayOfWeek = 1700557200,
            latitude = -1.267,
            longitude = 36.695,
            maxTemp = 23.16,
            minTemp = 15.38,
            eveTemp = 19.65,
            nightTemp = 15.58,
            dayTemp = 19.86,
            weatherId = 500,
            weatherMain = "Rain",
            weatherDesc = "light rain"
        ),
        ForecastEntity(
            dayOfWeek = 1700643600,
            latitude = -1.267,
            longitude = 36.695,
            maxTemp = 23.16,
            minTemp = 15.38,
            eveTemp = 19.65,
            nightTemp = 15.58,
            dayTemp = 19.86,
            weatherId = 500,
            weatherMain = "Rain",
            weatherDesc = "light rain"
        )
    )

    fun getFavoritePlaces(): List<FavoritePlacesEntity> = listOf(
        FavoritePlacesEntity(
            id = 192126,
            location = "Kikuyu",
            latitude = -1.267,
            longitude = 36.695,
            temp = 17.48,
            maxTemp = 17.53,
            minTemp = 15.81,
            weatherId = 802,
            weatherMain = "Clouds",
            weatherDesc = "scattered clouds",
            country = "KE",
            lastUpdatedAt = System.currentTimeMillis()
        ),
        FavoritePlacesEntity(
            id = 188492,
            location = "Machakos",
            latitude = -1.5177,
            longitude = 37.2634,
            temp = 17.48,
            maxTemp = 17.53,
            minTemp = 15.81,
            weatherId = 804,
            weatherMain = "Clouds",
            weatherDesc = "vercast clouds",
            country = "KE",
            lastUpdatedAt = System.currentTimeMillis()
        ),
        FavoritePlacesEntity(
            id = 183595,
            location = "Ngong",
            latitude = -1.3562,
            longitude = 36.6688,
            temp = 17.48,
            maxTemp = 17.53,
            minTemp = 15.81,
            weatherId = 802,
            weatherMain = "Clouds",
            weatherDesc = "broken clouds",
            country = "KE",
            lastUpdatedAt = System.currentTimeMillis()
        )
    )

}