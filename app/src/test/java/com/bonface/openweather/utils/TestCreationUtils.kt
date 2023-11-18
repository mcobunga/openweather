package com.bonface.openweather.utils

import android.location.Location
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.local.entity.ForecastEntity
import com.bonface.openweather.data.model.Clouds
import com.bonface.openweather.data.model.Coord
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.DailyForecast
import com.bonface.openweather.data.model.FeelsLike
import com.bonface.openweather.data.model.Main
import com.bonface.openweather.data.model.Temp
import com.bonface.openweather.data.model.Weather
import com.bonface.openweather.data.model.WeatherForecast
import retrofit2.Response

object TestCreationUtils {

    fun getApiKey(): String = java.util.UUID.randomUUID().toString()

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

    fun getMockLocation(): Location = Location("").apply {
        latitude = -1.267
        longitude = 36.695
    }

    fun handleResponse(response: Response<CurrentWeather>): Resource<CurrentWeather> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun handleForecastResponse(response: Response<WeatherForecast>): Resource<WeatherForecast> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun getCurrentWeatherResponse() = CurrentWeather(
        visibility = 10000,
        timezone = 10800,
        main = Main(temp = 17.48, feelsLike = null, tempMin = 15.81, tempMax = 17.53, pressure = null, seaLevel = null, grndLevel = null, humidity = null, tempKf = null),
        clouds = Clouds(all = 75),
        sys = null,
        dt = 1700285775,
        coord = Coord(lat = -1.267, lon = 36.695),
        weather = listOf(Weather(id = 500, main = "Rain", description = "light rain", icon = "10d")),
        name = "Kikuyu",
        cod = 200,
        id = 192126,
        base = "stations",
        wind = null
    )

    fun getWeatherForecastResponse() = WeatherForecast(
        timezone = "Africa/Nairobi",
        timezoneOffset = 10800,
        daily = listOf(DailyForecast(
            moonset = 1700338860,
            rain = 0.1,
            sunrise = 1700277188,
            temp = Temp( 14.78,21.86, 20.51, 15.4, 14.78, 14.78),
            moonPhase = 0.17,
            uvi = 14.36,
            moonrise = 1700293620,
            pressure = 1018,
            clouds = 73,
            feelsLike = FeelsLike(20.26, 15.35,20.69, 14.64),
            windGust = 8.06,
            dt = 1700298000,
            pop = 0.38,
            windDeg = 104,
            dewPoint = 12.68,
            sunset = 1700321036,
            weather = listOf(Weather(id = 500, main = "Rain", description = "light rain", icon = "10d")),
            humidity = 59,
            windSpeed = 5.56
        )),
        lat = -1.267,
        lon = 36.695
    )



}