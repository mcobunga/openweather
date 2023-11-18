package com.bonface.openweather.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonface.openweather.base.OpenWeatherBaseTest
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.TestCreationUtils.getApiKey
import com.bonface.openweather.utils.TestCreationUtils.getCurrentWeather
import com.bonface.openweather.utils.TestCreationUtils.getMockLocation
import com.bonface.openweather.utils.TestCreationUtils.getWeatherForecast
import com.bonface.openweather.utils.TestCreationUtils.handleForecastResponse
import com.bonface.openweather.utils.TestCreationUtils.handleResponse
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherRepositoryTest : OpenWeatherBaseTest() {

    private lateinit var weatherRepository: WeatherRepository

    @Before
    override fun setUp() {
        super.setUp()
        weatherRepository = WeatherRepository(
            openWeatherApi,
            currentWeatherDao,
            weatherForecastDao,
            favoritePlacesDao,
            getApiKey()
        )
    }

    @Test
    fun `should confirm fetching current weather info from remote data source`(): Unit = runBlocking {
        //given
        val currentWeather = weatherRepository.getCurrentLocationWeather(getMockLocation())
        Truth.assertThat(handleResponse(currentWeather) is Resource.Success)
    }


    @Test
    fun `should test saving current weather info into local db`() {
        runBlocking {
            weatherRepository.saveCurrentWeather(getCurrentWeather())
            val result = weatherRepository.getCurrentWeather().first().toList().first()
            MatcherAssert.assertThat(result.name, `is` (getCurrentWeather().name))
        }
    }


    @Test
    fun `should confirm fetching daily weather forecast from remote data source`(): Unit = runBlocking {
        val dailyForecast = weatherRepository.getCurrentLocationWeatherForecast(getMockLocation())
        Truth.assertThat(handleForecastResponse(dailyForecast) is Resource.Success)
    }


    @Test
    fun `should test saving daily weather forecast into local db`() {
        runBlocking {
            weatherRepository.saveWeatherForecast(getWeatherForecast().first())
            val result = weatherRepository.getWeatherForecast().first().toList().first()
            MatcherAssert.assertThat(result.dayOfWeek, `is` (getWeatherForecast().first().dayOfWeek))
        }
    }


}