package com.bonface.openweather.ui.home

import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.palette.graphics.Palette
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
    private var isAllFabsVisible: Boolean? = null

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
        setFloatingButtonController()
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
            favoritePlaces.setOnClickListener {

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
            getUserLocationCurrentWeather(location!!)
            getUserLocationWeatherForecast(location!!)
        }
    }

    private fun getUserLocationCurrentWeather(location: Location) {
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
        mainViewModel.getCurrentWeather(location)
    }

    private fun getUserLocationWeatherForecast(location: Location) {
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
        mainViewModel.getWeatherForecast(location)
    }

    private fun updateCurrentWeatherViews(current: CurrentWeather?) {
        with(binding) {
            current?.getCurrentWeatherBackgroundColor()
                ?.let { ContextCompat.getColor(this@MainActivity, it) }
                ?.let { weatherLayout.setBackgroundColor(it) }
            currentWeatherLayout.background = current?.getCurrentWeatherImage()
                ?.let { ContextCompat.getDrawable(this@MainActivity, it) }
            currentLocation.text = getString(R.string.user_location, current?.name.toString(), current?.sys?.country.toString())
            currentTemp.text = current?.main?.getTemperature()
            currentWeather.text = current?.weather?.firstOrNull()?.main
            lastUpdatedAt.text = getString(R.string.last_updated, current?.lastUpdated())
            minTemperatureValue.text = current?.main?.getMinTemperature()
            currentTemperatureValue.text = current?.main?.getTemperature()
            maxTemperatureValue.text = current?.main?.getMaxTemperature()
            current?.getCurrentWeatherImage()?.let { updateStatusBarColor(it) }

            addFavorite.setOnClickListener {
                current?.let { location -> mainViewModel.addToFavoritePlacesCurrentUserLocation(location) }
            }
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
        if (location != null) {
            mainViewModel.getCurrentWeather(location!!)
            mainViewModel.getWeatherForecast(location!!)
        }
    }

    private fun updateStatusBarColor(resource: Int) {
        val bitMap = BitmapFactory.decodeResource(resources, resource)
        Palette.Builder(bitMap).generate { result ->
            result?.let {
                val dominantSwatch = it.dominantSwatch
                if (dominantSwatch != null) {
                    val window: Window = window
                    window.statusBarColor = dominantSwatch.rgb
                }
            }
        }
    }

    private fun setFloatingButtonController() {
        isAllFabsVisible = false
        with(binding) {
            fab.shrink()
            fab.setOnClickListener {
                isAllFabsVisible = if (!isAllFabsVisible!!) {
                    favoritePlaces.show()
                    addFavorite.show()
                    favoritesActionText.visibility = View.VISIBLE
                    addFavActionText.visibility = View.VISIBLE
                    fab.extend()
                    true
                } else {
                    favoritePlaces.hide()
                    addFavorite.hide()
                    favoritesActionText.visibility = View.GONE
                    addFavActionText.visibility = View.GONE
                    fab.shrink()
                    false
                }
            }
            favoritePlaces.setOnClickListener {
                goToFavoritePlaces()
            }
        }
    }

    private fun goToFavoritePlaces() {

    }

}



