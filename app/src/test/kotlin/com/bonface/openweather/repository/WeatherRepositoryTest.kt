package com.bonface.openweather.repository

import com.bonface.openweather.data.network.OpenWeatherBaseTest
import com.bonface.openweather.domain.repository.WeatherRepository
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.TestCreationUtils.getApiKey
import com.bonface.openweather.utils.TestCreationUtils.getMockLocation
import com.bonface.openweather.utils.TestCreationUtils.getWeather
import com.bonface.openweather.utils.TestCreationUtils.handleResponse
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest : OpenWeatherBaseTest() {

    private lateinit var weatherRepository: WeatherRepository

    @Before
    override fun setUp() {
        super.setUp()
        weatherRepository = WeatherRepository(
            openWeatherApi,
            weatherDao,
            favoritePlacesDao,
            getApiKey()
        )
    }

    @Test
    fun `should confirm fetching weather info from remote data source`(): Unit = runBlocking {
        //given
        val currentWeather = weatherRepository.getCurrentLocationWeather(getMockLocation())
        Truth.assertThat(handleResponse(currentWeather) is Resource.Success)
    }


    @Test
    fun `should test saving weather info into local db`() {
        runBlocking {
            weatherRepository.saveWeather(getWeather())
            val result = weatherRepository.getWeather().first().toList().first()
            MatcherAssert.assertThat(result.name, `is` (getWeather().name))
        }
    }


}