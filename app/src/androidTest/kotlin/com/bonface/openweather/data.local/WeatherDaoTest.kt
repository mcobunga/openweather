package com.bonface.openweather.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.bonface.openweather.data.local.dao.WeatherDao
import com.bonface.openweather.utils.TestCreationUtils.getWeather
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@HiltAndroidTest
class WeatherDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_database")
    lateinit var database: OpenWeatherDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setup() {
        hiltRule.inject()
        weatherDao = database.weatherDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun saveWeatherData() = runTest {
        //given
        val weatherInfo = getWeather()
        //when
        weatherDao.saveWeather(weatherInfo)
        val result = weatherDao.getWeather().take(1).toList().firstOrNull()
        //verify
        assertEquals(result?.firstOrNull()?.name, weatherInfo.name)
    }

    @Test
    fun deleteWeatherInfoByLocationFromDatabase () = runTest {
        //given
        val weatherInfo = getWeather()
        //when
        weatherDao.saveWeather(weatherInfo)
        weatherDao.deleteWeatherInfoByLocationName(weatherInfo.name.toString())

        val result = weatherDao.getWeather().take(1).toList().firstOrNull()
        assertEquals(result?.firstOrNull(), null)
    }

    @Test
    fun deleteWeatherInfoFromDatabase () = runTest {
        //given
        val weatherInfo = getWeather()
        //when
        weatherDao.saveWeather(weatherInfo)
        weatherDao.deleteWeatherInfo(weatherInfo)

        val result = weatherDao.getWeather().take(1).toList().firstOrNull()
        assertEquals(result?.firstOrNull(), null)
    }

    @Test
    fun nukeTable () = runTest {
        //given
        val weatherInfo = getWeather()
        //when
        weatherDao.saveWeather(weatherInfo)
        weatherDao.nukeTable()

        val result = weatherDao.getWeather().take(1).toList().firstOrNull()
        assertEquals(result?.firstOrNull(), null)
    }

    @Test
    fun countWeatherInfoItemsSaved () = runTest {
        //given
        val weatherInfo = getWeather()
        val weatherInfo2 = weatherInfo.copy(id = 192127, "Eldoret")
        val weatherInfo3 = weatherInfo.copy(id = 192128, name = "Maseno")
        //when
        weatherDao.saveWeather(weatherInfo)
        weatherDao.saveWeather(weatherInfo2)
        weatherDao.saveWeather(weatherInfo3)
        val result = weatherDao.getCount().take(1).toList()
        assertEquals(result.firstOrNull(), 3)
    }

}