package com.bonface.openweather.mappers

import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.WeatherEntity
import com.bonface.openweather.domain.model.CurrentWeather
import com.bonface.openweather.domain.model.DailyWeather
import com.bonface.openweather.domain.model.WeatherForecast
import com.bonface.openweather.utils.roundOffLatLonToHalfUp

fun WeatherEntity.toFavoritePlacesEntity(): FavoritePlacesEntity =  FavoritePlacesEntity(
    id = id,
    location = name,
    latitude = roundOffLatLonToHalfUp(latitude!!),
    longitude = roundOffLatLonToHalfUp(longitude!!),
    temp = temp,
    maxTemp = maxTemp,
    minTemp = minTemp,
    weatherId = weatherId,
    weatherMain = weatherMain,
    weatherDesc = weatherDesc,
    country = country,
    lastUpdatedAt = System.currentTimeMillis(),
)

fun Pair<CurrentWeather, WeatherForecast>.toWeatherEntity(): WeatherEntity {
    return WeatherEntity(
        id = first.id!!,
        name = first.name.toString(),
        latitude = roundOffLatLonToHalfUp(first.coord?.lat!!),
        longitude = roundOffLatLonToHalfUp(first.coord?.lon!!),
        dt = first.dt,
        temp = first.main?.temp,
        maxTemp = first.main?.tempMax,
        minTemp = first.main?.tempMin,
        weatherId = first.weather?.firstOrNull()?.id,
        weatherMain = first.weather?.firstOrNull()?.main.toString(),
        weatherDesc = first.weather?.firstOrNull()?.description.toString(),
        country = first.sys?.country,
        forecast = second.toDailyWeather(),
        lastUpdatedAt = System.currentTimeMillis()
    )
}

fun WeatherForecast.toDailyWeather(): List<DailyWeather> {
    val forecast: MutableList<DailyWeather> = ArrayList()
    daily?.forEach {
        val dailyForecast = DailyWeather(
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
        forecast.add(dailyForecast)
    }
    return forecast.toMutableList()
}