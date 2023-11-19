package com.bonface.openweather.mappers

import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.ForecastEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.utils.roundOffLatLonToHalfUp

fun CurrentWeather.toWeatherEntity(): CurrentWeatherEntity = CurrentWeatherEntity(
        id = id!!,
        name = name.toString(),
        latitude = roundOffLatLonToHalfUp(coord?.lat!!),
        longitude = roundOffLatLonToHalfUp(coord.lon!!),
        dt = dt,
        temp = main?.temp,
        maxTemp = main?.tempMax,
        minTemp = main?.tempMin,
        weatherId = weather?.firstOrNull()?.id,
        weatherMain = weather?.firstOrNull()?.main.toString(),
        weatherDesc = weather?.firstOrNull()?.description.toString(),
        country = sys?.country,
        lastUpdatedAt = System.currentTimeMillis()
    )

fun WeatherForecast.toDailyForecastEntity(): List<ForecastEntity> {
    val weatherForecast: MutableList<ForecastEntity> = ArrayList()
    daily?.forEach {
        val dailyForecastEntity = ForecastEntity(
            dayOfWeek = it.dt,
            latitude = roundOffLatLonToHalfUp(lat!!),
            longitude = roundOffLatLonToHalfUp(lon!!),
            maxTemp = it.temp?.max,
            minTemp = it.temp?.min,
            eveTemp = it.temp?.eve,
            nightTemp = it.temp?.night,
            dayTemp = it.temp?.day,
            weatherId = it.weather?.firstOrNull()?.id,
            weatherMain = it.weather?.firstOrNull()?.main,
            weatherDesc = it.weather?.firstOrNull()?.description
        )
        weatherForecast.add(dailyForecastEntity)
    }
    return weatherForecast.toMutableList()
}


fun CurrentWeather.toFavoritePlacesEntity(): FavoritePlacesEntity =  FavoritePlacesEntity(
    id = id!!,
    location = name,
    latitude = roundOffLatLonToHalfUp(coord?.lat!!),
    longitude = roundOffLatLonToHalfUp(coord.lon!!),
    temp = main?.temp,
    maxTemp = main?.tempMax,
    minTemp = main?.tempMin,
    weatherId = weather?.firstOrNull()?.id,
    weatherMain = weather?.firstOrNull()?.main.toString(),
    weatherDesc = weather?.firstOrNull()?.description.toString(),
    country = sys?.country,
    lastUpdatedAt = System.currentTimeMillis()
)