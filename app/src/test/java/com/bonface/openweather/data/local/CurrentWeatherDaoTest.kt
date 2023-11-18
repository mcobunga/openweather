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

@RunWith(AndroidJUnit4::class)
class CurrentWeatherDaoTest: OpenWeatherBaseTest() {

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `save current weather to current weather table and verify`() = runBlocking {
        //given
        val currentWeather = TestCreationUtils.getCurrentWeather()

        //when
        currentWeatherDao.saveCurrentWeather(currentWeather)
        val weather = currentWeatherDao.getCurrentWeather().first().toList()

        //verify
        MatcherAssert.assertThat(weather.first().dt, `is` (currentWeather.dt))

    }

}