package com.bonface.openweather.ui.home

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.mappers.toCurrentUserLocationEntity
import com.bonface.openweather.mappers.toOtherLocationEntity
import com.bonface.openweather.mappers.toWeatherForecastEntity
import com.bonface.openweather.repository.WeatherRepository
import com.bonface.openweather.utils.ErrorHandler
import com.bonface.openweather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _current = MutableLiveData<Resource<CurrentWeather>>()
    private val _forecast = MutableLiveData<Resource<WeatherForecast>>()
    private val _favorites = MutableLiveData<Resource<WeatherForecast>>()
    private var _currentLocation: MutableLiveData<Location> = MutableLiveData()

    val currentWeather: LiveData<Resource<CurrentWeather>>
        get() = _current
    val forecastWeather: LiveData<Resource<WeatherForecast>>
        get() = _forecast
    val favoritePlaces: LiveData<Resource<WeatherForecast>>
        get() = _favorites
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    fun getCurrentWeather(location: Location) {
        _forecast.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.getCurrentWeatherByLocation(location).collect{
                    _current.postValue(handleResponse(it))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _forecast.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    fun getWeatherForecast(location: Location) {
        _forecast.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.getWeatherForecastByLocation(location).collect{
                    _forecast.postValue(handleForecastResponse(it))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _forecast.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    fun addToFavoritePlacesCurrentUserLocation(currentWeather: CurrentWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.saveCurrentUserLocation(
                currentWeather.toCurrentUserLocationEntity()
            )
        }
    }

    fun addToFavoritePlacesUserLocation(currentWeather: CurrentWeather) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.saveCurrentUserLocation(
                currentWeather.toOtherLocationEntity()
            )
        }
    }

    fun saveWeatherForecast(weatherForecast: WeatherForecast, currentLocation: String) =
        viewModelScope.launch(Dispatchers.IO) {
            weatherForecast.toWeatherForecastEntity(currentLocation).toMutableList().forEach {
                weatherRepository.saveWeatherForecast(it)
            }
        }

    fun getAllWeatherForecast() = weatherRepository.getAllWeatherForecast()

    fun getFavoritePlaces() = weatherRepository.getAllFavoritePlaces()

    fun getCurrentUserLocation() = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.getCurrentUserLocation()
    }

    fun removeFavoritePlace(favoritePlacesEntity: FavoritePlacesEntity) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.removeFavoritePlace(favoritePlacesEntity)
    }

    fun deleteAllPlaces() = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.deleteAllPlaces()
    }

    fun deleteAllWeatherForecast(favoritePlacesEntity: FavoritePlacesEntity) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.deleteAllWeatherForecast()
    }

    fun deleteWeatherForecast(location: String) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.deleteWeatherForecast(location)
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


}