package com.bonface.openweather.ui.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.mappers.toDailyForecastEntity
import com.bonface.openweather.mappers.toFavoritePlacesEntity
import com.bonface.openweather.mappers.toWeatherEntity
import com.bonface.openweather.repository.WeatherRepository
import com.bonface.openweather.utils.ErrorHandler
import com.bonface.openweather.utils.LocationProvider
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.roundOffLatLonToHalfUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider,

) : ViewModel() {

    private val _current = MutableStateFlow<Resource<CurrentWeather>>(Resource.Loading())
    private val _forecast = MutableStateFlow<Resource<WeatherForecast>>(Resource.Loading())
    private var _currentLocation = MutableStateFlow<Location?>(null)
    private var _isExists = MutableStateFlow<Boolean?>(false)

    val currentWeather = _current.asStateFlow()
    val forecastWeather = _forecast.asStateFlow()
    val currentLocation = _currentLocation.asStateFlow()
    val isExists = _isExists.asStateFlow()

    fun getCurrentWeatherFromRemote(location: Location) {
        _current.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = weatherRepository.getCurrentLocationWeather(location)
                _current.value = (handleResponse(result))
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _current.value = (Resource.Error(error.message.toString()))
            }
        }
    }

    fun getWeatherForecastFromRemote(location: Location) {
        _forecast.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = weatherRepository.getCurrentLocationWeatherForecast(location)
                _forecast.value = handleForecastResponse(result)
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _forecast.value = Resource.Error(error.message.toString())
            }
        }
    }

    fun saveCurrentWeatherToDb(currentWeather: CurrentWeather) = viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.saveCurrentWeather(currentWeather.toWeatherEntity())
        }

    fun saveWeatherForecastToDb(weatherForecast: WeatherForecast) = viewModelScope.launch(Dispatchers.IO) {
            weatherForecast.toDailyForecastEntity().toMutableList().forEach {
                weatherRepository.saveWeatherForecast(it)
            }
        }

    fun getCurrentWeatherFromDb() = weatherRepository.getCurrentWeather()

    fun getWeatherForecastFromDb() = weatherRepository.getWeatherForecast()

    fun deleteCurrentWeatherFromDb() = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.deleteCurrentWeather()
    }

    fun deleteWeatherForecast() = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.deleteWeatherForecast()
    }


    fun saveLocationToFavorites(currentWeather: CurrentWeather) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.saveFavoritePlace(currentWeather.toFavoritePlacesEntity())
    }

    fun getFavoritePlaces() = weatherRepository.getFavoritePlaces()

    fun isLocationAlreadyExists(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        try {
            weatherRepository.isLocationAlreadyExists(roundOffLatLonToHalfUp(location.latitude), roundOffLatLonToHalfUp(location.longitude)).apply {
                _isExists.value = this
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun handleResponse(response: Response<CurrentWeather>): Resource<CurrentWeather> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }

    private fun handleForecastResponse(response: Response<WeatherForecast>): Resource<WeatherForecast> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun getCurrentLocation() = viewModelScope.launch(Dispatchers.IO) {
        locationProvider.getLocation().collect { location ->
            _currentLocation.value = location
            cancel()
        }
    }

}