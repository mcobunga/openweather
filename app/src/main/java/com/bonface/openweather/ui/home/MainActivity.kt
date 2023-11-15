package com.bonface.openweather.ui.home

import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonface.openweather.R
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.databinding.ActivityMainBinding
import com.bonface.openweather.utils.PermissionUtils.getUserCurrentLocation
import com.bonface.openweather.utils.PermissionUtils.isAccessFineLocationGranted
import com.bonface.openweather.utils.PermissionUtils.isLocationEnabled
import com.bonface.openweather.utils.PermissionUtils.requestAccessFineLocationPermission
import com.bonface.openweather.utils.PermissionUtils.showEnableGPSDialog
import com.bonface.openweather.utils.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var forecastAdapter: ForecastAdapter
    private var location: Location? = null

    private val viewModel: SplashViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.loading.value
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        checkForLocationPermission()
        getUserLocationWeatherInfo()
    }

    private fun setupUI() {
        with(binding) {
            weatherForecast.apply {
                adapter = forecastAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
            tapToRefresh.setOnClickListener {
                refreshWeatherData()
            }
        }
    }

    private fun checkForLocationPermission() {
        when {
            isAccessFineLocationGranted(this) -> {
                when {
                    isLocationEnabled(this) -> {
                        location = getUserCurrentLocation(this,this)
                        getUserLocationWeatherInfo()
                    }
                    else -> showEnableGPSDialog(this)
                }
            }
            else -> requestAccessFineLocationPermission(this,this )
        }
    }

    private fun getUserLocationWeatherInfo() {
        if (location?.latitude != null && location?.longitude != null) {
            getUserLocationCurrentWeather()
            getUserLocationWeatherForecast()
        }
    }

    private fun getUserLocationCurrentWeather() {
        mainViewModel.currentWeather.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideLoading()
                    updateCurrentWeatherViews(resource.data)
                }
                is Resource.Error -> {
                    hideLoading()
                    showSnackbarErrorMessage(resource.message.toString())
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun getUserLocationWeatherForecast() {
        mainViewModel.forecastWeather.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideLoading()
                    updateWeatherForecastViews(resource.data)
                }
                is Resource.Error -> {
                    hideLoading()
                    showSnackbarErrorMessage(resource.message.toString())
                }
                is Resource.Loading -> {
                    showLoading()
                }
            }
        }
    }

    private fun updateCurrentWeatherViews(current: CurrentWeather?) {
        with(binding) {
            currentLocation.text = getString(R.string.user_location, current?.name.toString(), current?.sys?.country.toString())
            currentTemp.text = current?.main?.getTemperature()
            currentWeather.text = current?.weather?.firstOrNull()?.main
            lastUpdatedAt.text = getString(R.string.last_updated, current?.lastUpdated())
            minTemperatureValue.text = current?.main?.getMinTemperature()
            currentTemperatureValue.text = current?.main?.getTemperature()
            maxTemperatureValue.text = current?.main?.getMaxTemperature()
        }
    }

    private fun updateWeatherForecastViews(forecast: WeatherForecast?) {
        forecastAdapter.differ.submitList(forecast?.daily?.take(5))
    }

    private fun showLoading() {
        binding.loadingLayout.apply {
            visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.loadingLayout.apply {
            visibility = View.GONE
        }
    }

    private fun showSnackbarErrorMessage(message: String) {
        binding.weatherLayout.apply {
            val snack = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again)) {
                refreshWeatherData()
            }
            val button = snack.view.findViewById(com.google.android.material.R.id.snackbar_action) as TextView
            button.apply {
                isAllCaps = true
                ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
            }
            snack.show()
        }
    }

    private fun refreshWeatherData() {
        mainViewModel.getCurrentWeather()
        mainViewModel.getWeatherForecast()
    }

}



