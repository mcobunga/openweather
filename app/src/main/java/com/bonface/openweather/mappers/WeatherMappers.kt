package com.bonface.openweather.mappers

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.bonface.openweather.data.local.entity.ForecastEntity
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast

fun CurrentWeather.toWeatherEntity(): CurrentWeatherEntity = CurrentWeatherEntity(
        id = id!!,
        name = name.toString(),
        latitude = coord?.lat,
        longitude = coord?.lon,
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
            latitude = lat,
            longitude = lon,
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
    latitude = coord?.lat,
    longitude = coord?.lon,
    temp = main?.temp,
    maxTemp = main?.tempMax,
    minTemp = main?.tempMin,
    weatherId = weather?.firstOrNull()?.id,
    weatherMain = weather?.firstOrNull()?.main.toString(),
    weatherDesc = weather?.firstOrNull()?.description.toString(),
    country = sys?.country,
    lastUpdatedAt = System.currentTimeMillis()
)