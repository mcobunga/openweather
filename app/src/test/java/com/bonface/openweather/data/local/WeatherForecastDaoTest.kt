package com.bonface.openweather.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonface.openweather.base.OpenWeatherBaseTest
import com.bonface.openweather.utils.TestCreationUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
class WeatherForecastDaoTest: OpenWeatherBaseTest() {

    @get: Rule
    val rule = InstantTaskExecutorRule()


    @Test
    fun `save weather forecast to weather forecast table and verify`() = runBlocking {
        //given
        val weatherForecast = TestCreationUtils.getWeatherForecast()

        //when
        weatherForecastDao.saveWeatherWeather(weatherForecast.first())
        val forecast = weatherForecastDao.getWeatherWeather().first().toList()

        //verify
        MatcherAssert.assertThat(forecast.first().dayOfWeek, `is` (weatherForecast.first().dayOfWeek))

    }

}