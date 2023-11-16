package com.bonface.openweather.mappers

import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.WeatherForeCastEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast

fun CurrentWeather.toCurrentUserLocationEntity(): FavoritePlacesEntity = FavoritePlacesEntity(
        location = name.toString(),
        latitude = coord?.lat!!,
        longitude = coord.lon,
        temperature = main?.temp,
        maxTemp = main?.tempMax,
        minTemp = main?.tempMin,
        weatherId = weather?.firstOrNull()?.id,
        weatherMain = weather?.firstOrNull()?.main.toString(),
        weatherDesc = weather?.firstOrNull()?.description.toString(),
        isCurrentUserLocation = true,
        lastUpdatedAt = System.currentTimeMillis()
    )


fun CurrentWeather.toOtherLocationEntity(): FavoritePlacesEntity {

    return FavoritePlacesEntity(
        location = name.toString(),
        latitude = coord?.lat!!,
        longitude = coord.lon,
        temperature = main?.temp,
        maxTemp = main?.tempMax,
        minTemp = main?.tempMin,
        weatherId = weather?.firstOrNull()?.id,
        weatherMain = weather?.firstOrNull()?.main.toString(),
        weatherDesc = weather?.firstOrNull()?.description.toString(),
        isCurrentUserLocation = false,
        lastUpdatedAt = System.currentTimeMillis()
    )
}


fun WeatherForecast.toWeatherForecastEntity(location: String): List<WeatherForeCastEntity> {
    val weatherForecast: MutableList<WeatherForeCastEntity> = ArrayList()
    daily?.forEach {
        val foreCastEntity = WeatherForeCastEntity(
            day = it.dt,
            location = location,
            latitude = lat!!,
            longitude = lon,
            temperature = it.temp?.eve,
            maxTemp = it.temp?.max,
            minTemp = it.temp?.min,
            weatherId = it.weather?.firstOrNull()?.id,
            weatherMain = it.weather?.firstOrNull()?.main,
            weatherDesc = it.weather?.firstOrNull()?.description,
            lastUpdatedAt = it.dt
        )
        weatherForecast.add(foreCastEntity)
    }

    return weatherForecast
}