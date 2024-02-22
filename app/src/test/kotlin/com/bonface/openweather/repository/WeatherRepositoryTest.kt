package com.bonface.openweather.repository

import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.WeatherDao
import com.bonface.openweather.data.network.OpenWeatherApiTest
import com.bonface.openweather.domain.repository.WeatherRepository
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.TestCreationUtils.getMockLocation
import com.bonface.openweather.utils.TestCreationUtils.getWeather
import com.bonface.openweather.utils.TestCreationUtils.handleResponse
import com.google.common.truth.Truth
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest : OpenWeatherApiTest() {

    @Mock
    private lateinit var weatherRepository: WeatherRepository
    private val weatherDao = mockk<WeatherDao>(relaxed = true)
    private val favoritePlacesDao = mockk<FavoritePlacesDao>(relaxed = true)

    @Before
    override fun before() {
        super.before()
        weatherRepository = WeatherRepository(
            openWeatherApi,
            weatherDao,
            favoritePlacesDao
        )
    }

    @After
    override fun after() {
        super.after()
        clearAllMocks()
    }

    @Test
    fun `should confirm fetching weather info from remote data source`() = runTest {
        //given
        val currentWeather = weatherRepository.getCurrentLocationWeather(getMockLocation())
        Truth.assertThat(handleResponse(currentWeather) is Resource.Success)
    }


    @Test
    fun `should test saving weather info into local db`() = runTest {
        //given
        val weatherInfo = getWeather()
        weatherRepository.saveWeather(weatherInfo)
        val result = weatherRepository.getWeather().collect {
            MatcherAssert.assertThat(it.firstOrNull()?.name, `is` (weatherInfo.name))
        }
    }


}