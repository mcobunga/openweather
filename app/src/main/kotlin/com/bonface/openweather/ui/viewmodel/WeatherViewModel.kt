package com.bonface.openweather.ui.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonface.openweather.data.local.entity.WeatherEntity
import com.bonface.openweather.di.NetworkModule
import com.bonface.openweather.domain.usecases.WeatherUseCase
import com.bonface.openweather.mappers.toFavoritePlacesEntity
import com.bonface.openweather.utils.LocationProvider
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.roundOffLatLonToHalfUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val locationProvider: LocationProvider,
    @NetworkModule.IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var _currentLocation = MutableStateFlow<Location?>(null)
    private var _isExists = MutableStateFlow<Boolean?>(false)
    val currentLocation = _currentLocation.asStateFlow()
    val isExists = _isExists.asStateFlow()

    fun getWeatherFromRemote(location: Location) {
        viewModelScope.launch(dispatcher) {
            weatherUseCase.invoke(location).collect { result ->
                when(result) {
                    is Resource.Success -> _uiState.value = WeatherUiState.Success(result.data)
                    is Resource.Error -> _uiState.value = WeatherUiState.Error(result.message.toString())
                    is Resource.Loading -> _uiState.value = WeatherUiState.Loading
                }
            }
        }
    }

    fun saveWeather(weather: WeatherEntity) = viewModelScope.launch(dispatcher) {
        weatherUseCase.saveWeather(weather)
        }

    fun getWeatherFromCache() = weatherUseCase.getWeather()

    fun deleteWeather() = viewModelScope.launch(dispatcher) {
        weatherUseCase.deleteWeather()
    }

    fun saveLocationToFavorites(weatherEntity: WeatherEntity) = viewModelScope.launch(dispatcher) {
        weatherUseCase.saveFavoritePlace(weatherEntity.toFavoritePlacesEntity())
    }

    fun getFavoritePlaces() = weatherUseCase.getFavoritePlaces()

    fun isLocationAlreadyExists(location: Location) = viewModelScope.launch(dispatcher) {
        try {
            weatherUseCase.isLocationAlreadyExists(roundOffLatLonToHalfUp(location.latitude), roundOffLatLonToHalfUp(location.longitude)).apply {
                _isExists.value = this
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCurrentLocation() = viewModelScope.launch(dispatcher) {
        locationProvider.getLocation().collect { location ->
            _currentLocation.value = location
            cancel()
        }
    }

}

sealed class WeatherUiState {
    data object Loading : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
    data class Success(val weather: WeatherEntity?) : WeatherUiState()
}
