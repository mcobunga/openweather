package com.bonface.openweather.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.repository.WeatherRepository
import com.bonface.openweather.utils.ErrorHandler
import com.bonface.openweather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _current = MutableLiveData<Resource<CurrentWeather>>()
    private val _forecast = MutableLiveData<Resource<WeatherForecast>>()
    val currentWeather: LiveData<Resource<CurrentWeather>>
        get() = _current
    val forecastWeather: LiveData<Resource<WeatherForecast>>
        get() = _forecast

    init {
        getCurrentWeather()
        getWeatherForecast()
    }

    fun getCurrentWeather() {
        _forecast.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.getCurrentWeatherByLocation("-1.266931", "36.694951").collect{
                    _current.postValue(handleResponse(it))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _forecast.postValue(Resource.Error(error.message.toString()))
            }
        }
    }

    fun getWeatherForecast() {
        _forecast.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository.getWeatherForecastByLocation("-1.266931", "36.694951").collect{
                    _forecast.postValue(handleForecastResponse(it))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val error = ErrorHandler.handleException(e)
                _forecast.postValue(Resource.Error(error.message.toString()))
            }
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


}