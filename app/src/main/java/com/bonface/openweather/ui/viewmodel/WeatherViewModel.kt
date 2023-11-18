package com.bonface.openweather.ui.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.bonface.openweather.utils.roundOffLatLonDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val _current = MutableLiveData<Resource<CurrentWeather>>()
    private val _forecast = MutableLiveData<Resource<WeatherForecast>>()
    private var _currentLocation: MutableLiveData<Location> = MutableLiveData()
    private var _isExists: MutableLiveData<Boolean> = MutableLiveData()

    val currentWeather: LiveData<Resource<CurrentWeather>>
        get() = _current
    val forecastWeather: LiveData<Resource<WeatherForecast>>
        get() = _forecast

    val currentLocation: LiveData<Location>
        get() = _currentLocation
    val isExists: LiveData<Boolean>
        get() = _isExists

    fun getCurrentWeatherFromRemote(location: Location) {
        _current.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = weatherRepository.getCurrentLocationWeather(location)
                _current.postValue(handleResponse(result))
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _current.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    fun getWeatherForecastFromRemote(location: Location) {
        _forecast.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = weatherRepository.getCurrentLocationWeatherForecast(location)
                _forecast.postValue(handleForecastResponse(result))
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _forecast.postValue(Resource.Error(error.message.toString()))
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
            weatherRepository.isLocationAlreadyExists(roundOffLatLonDecimal(location.latitude), roundOffLatLonDecimal(location.longitude)).apply {
                _isExists.postValue(this)
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
            _currentLocation.postValue(location)
            cancel()
        }
    }


}