package com.bonface.openweather.ui.viewmodel

import app.cash.turbine.test
import com.bonface.openweather.data.local.entity.WeatherEntity
import com.bonface.openweather.domain.usecases.WeatherUseCase
import com.bonface.openweather.utils.BaseTest
import com.bonface.openweather.utils.LocationProvider
import com.bonface.openweather.utils.MainDispatcherRule
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.TestCreationUtils.getMockLocation
import com.bonface.openweather.utils.TestCreationUtils.getWeather
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest: BaseTest() {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val dispatcher = UnconfinedTestDispatcher()

    private val locationProvider = mockk<LocationProvider>(relaxed = true)
    private val weatherUseCase = mockk<WeatherUseCase>(relaxed = true)
    @Mock
    private lateinit var viewModel: WeatherViewModel

    @Before
    override fun setup() {
        super.setup()
        viewModel = WeatherViewModel(
            weatherUseCase,
            locationProvider,
            dispatcher
        )
    }

    @After
    override fun teardown() {
        super.teardown()
        clearAllMocks()
    }

    @Test
    fun `Given that viewmodel getWeatherFromRemote has been initiated, make sure that we show a loading state`() = runBlocking {
        val result = MutableStateFlow<Resource<WeatherEntity>>(Resource.Loading())
        // GIVEN
        coEvery {
            weatherUseCase.invoke(getMockLocation())
        } returns result
        // WHEN
        viewModel.getWeatherFromRemote(getMockLocation())
        //THEN
        viewModel.uiState.test {
            assertEquals(WeatherUiState.Loading, awaitItem())
            assert(viewModel.uiState.value is WeatherUiState.Loading)
        }
    }

    @Test
    fun `Given that getWeatherFromRemote api call return success, make sure that we show a success state`() = runBlocking {
        val result = MutableStateFlow<Resource<WeatherEntity>>(Resource.Success(getWeather()))
        // GIVEN
        coEvery {
            weatherUseCase.invoke(getMockLocation())
        } returns result
        // WHEN
        viewModel.getWeatherFromRemote(getMockLocation())
        //THEN
        viewModel.uiState.test {
            assert(viewModel.uiState.value is WeatherUiState.Success)
            assertEquals(WeatherUiState.Success(getWeather()), awaitItem())
            assertNotNull((viewModel.uiState.value as WeatherUiState.Success).weather)
            assertEquals((viewModel.uiState.value as WeatherUiState.Success).weather?.weatherId, getWeather().weatherId)
        }
    }

    @Test
    fun `Given that getWeatherFromRemote api call returns an error, make sure that we emit error state`() = runBlocking {
        val result = MutableStateFlow<Resource<WeatherEntity>>(Resource.Error(message = "Something went wrong", data = null))
        // GIVEN
        coEvery {
            weatherUseCase.invoke(getMockLocation())
        } returns result
        // WHEN
        viewModel.getWeatherFromRemote(getMockLocation())
        //THEN
        viewModel.uiState.test {
            assert(viewModel.uiState.value is WeatherUiState.Error)
            assertEquals(WeatherUiState.Error(message = "Something went wrong"), awaitItem())
            assertEquals((viewModel.uiState.value as WeatherUiState.Error).message, "Something went wrong")
        }
    }


}