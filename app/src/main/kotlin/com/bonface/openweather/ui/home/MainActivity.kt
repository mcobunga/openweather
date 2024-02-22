package com.bonface.openweather.ui.home

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonface.openweather.R
import com.bonface.openweather.data.local.entity.WeatherEntity
import com.bonface.openweather.databinding.ActivityMainBinding
import com.bonface.openweather.ui.favorites.FavoritePlacesActivity
import com.bonface.openweather.ui.home.adapter.ForecastAdapter
import com.bonface.openweather.ui.search.SearchPlacesActivity
import com.bonface.openweather.ui.viewmodel.SplashViewModel
import com.bonface.openweather.ui.viewmodel.WeatherUiState
import com.bonface.openweather.ui.viewmodel.WeatherViewModel
import com.bonface.openweather.utils.getCurrentWeatherBackgroundColor
import com.bonface.openweather.utils.getCurrentWeatherImage
import com.bonface.openweather.utils.getTemperature
import com.bonface.openweather.utils.gone
import com.bonface.openweather.utils.isAccessFineLocationGranted
import com.bonface.openweather.utils.isLocationEnabled
import com.bonface.openweather.utils.lastUpdated
import com.bonface.openweather.utils.onBackPressedCallback
import com.bonface.openweather.utils.show
import com.bonface.openweather.utils.showEnableGPSDialog
import com.bonface.openweather.utils.startActivity
import com.bonface.openweather.utils.toast
import com.bonface.openweather.utils.updateStatusBarColor
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var forecastAdapter: ForecastAdapter
    private var location: Location? = null
    private val splashViewModel: SplashViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var isFabsVisible: Boolean? = false
    private var isCacheAvailable: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.loading.value
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        checkLocationPermission()
        setLocationObserver()
        setFloatingButtonController()
        getCachedWeatherInfo()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback())
    }

    override fun onResume() {
        super.onResume()
        isFabsVisible = false
    }

    override fun onStop() {
        super.onStop()
        location = null
    }

    private fun setupUI() {
        with(binding) {
            weatherForecast.apply {
                adapter = forecastAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
            tapToRefresh.setOnClickListener { refreshWeatherData() }
            favoritePlaces.setOnClickListener { goToFavorites() }
            searchPlaces.setOnClickListener { goToSearch() }
            addFavorite.setOnClickListener { saveToFavoriteLocations() }
            searchLocations.setOnClickListener { goToSearch() }
            markFavorite.setOnClickListener { saveToFavoriteLocations() }
        }
    }

    private fun checkLocationPermission() {
        when {
            isAccessFineLocationGranted() -> {
                when {
                    isLocationEnabled() -> weatherViewModel.getCurrentLocation()
                    else -> showEnableGPSDialog()
                }
            }
            else -> requestAccessFineLocationPermission()
        }
    }


    private fun setLocationObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.currentLocation.collect {
                    location = it
                    it?.let { location ->
                        getWeatherFromRemote(location)
                    }
                }
            }
        }
    }

    private fun getCachedWeatherInfo() {
        lifecycleScope.launch {
            weatherViewModel.getWeatherFromCache().collect { weather ->
                if (weather.isNotEmpty()) {
                    isCacheAvailable = true
                    updateWeatherViews(weather)
                    forecastAdapter.differ.submitList(weather.firstOrNull()?.forecast)
                }
            }
        }
    }

    private fun getWeatherFromRemote(location: Location) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is WeatherUiState.Success -> {
                            hideLoading()
                            cacheWeatherData(uiState.weather)
                        }
                        is WeatherUiState.Error -> {
                            if (isCacheAvailable == true) hideLoading()
                            showSnackbarErrorMessage(uiState.message)
                        }
                        is WeatherUiState.Loading -> {
                            if (isCacheAvailable == true) hideLoading() else showLoading()
                        }
                    }
                }
            }
        }
        weatherViewModel.getWeatherFromRemote(location)
    }

    private fun cacheWeatherData(weatherEntity: WeatherEntity?) {
        weatherEntity?.let { weather ->
            weatherViewModel.deleteWeather().invokeOnCompletion {
                weatherViewModel.saveWeather(weather)
            }
        }
        weatherInfo = weatherEntity
    }

    private fun updateWeatherViews(weather: List<WeatherEntity>) {
        with(binding) {
            weather.firstOrNull()?.apply {
                weatherLayout.setBackgroundColor(ContextCompat.getColor(this@MainActivity, getCurrentWeatherBackgroundColor(this.weatherId)))
                currentWeatherLayout.background = ContextCompat.getDrawable(this@MainActivity, getCurrentWeatherImage(this.weatherId))
                currentLocation.text = getString(R.string.user_location, this.name.toString(), this.country.toString())
                currentTemp.text = getTemperature(this.temp)
                currentWeather.text = this.weatherMain
                updatedAt.text = getString(R.string.last_updated, lastUpdated(this.lastUpdatedAt))
                minTemperatureValue.text = getTemperature(this.minTemp)
                currentTemperatureValue.text = getTemperature(this.temp)
                maxTemperatureValue.text = getTemperature(this.maxTemp)
                updateStatusBarColor(getCurrentWeatherImage(this.weatherId))
            }
        }
    }

    private fun showSnackbarErrorMessage(message: String) {
        binding.weatherLayout.apply {
            val snack = Snackbar.make(this, message, Snackbar.LENGTH_LONG).setAction(getString(R.string.try_again)) {
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
            weatherViewModel.getWeatherFromRemote(location!!)
        } else {
            toast(getString(R.string.enable_location_permissions))
        }
    }

    private fun setFloatingButtonController() {
        with(binding) {
            fab.shrink()
            fab.setOnClickListener {
                isFabsVisible = if (!isFabsVisible!!) {
                    showFabOptions()
                    true
                } else {
                    hideFabOptions()
                    false
                }
            }
        }
    }

    private fun showFabOptions() {
        with(binding) {
            favoritePlaces.show()
            if (isCacheAvailable == true) addFavorite.show() else addFavorite.hide()
            if (isCacheAvailable == true) addFavActionText.show() else addFavActionText.gone()
            searchPlaces.show()
            favoritesActionText.show()
            searchActionText.show()
            fab.extend()
        }
    }

    private fun hideFabOptions() {
        with(binding) {
            favoritePlaces.hide()
            addFavorite.hide()
            searchPlaces.hide()
            favoritesActionText.gone()
            addFavActionText.gone()
            searchActionText.gone()
            fab.shrink()
        }
    }

    private fun requestAccessFineLocationPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(object : PermissionListener {
                override fun onPermissionGranted(ermissionGrantedResponse: PermissionGrantedResponse) {
                    weatherViewModel.getCurrentLocation()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    toast(getString(R.string.you_need_to_allow_location_permission))
                    finish()
                }

                override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken) {
                    showEnableGPSDialog()
                }

        }).check()
    }

    private fun saveToFavoriteLocations() {
        if (location != null) {
            lifecycleScope.launch {
                weatherViewModel.isExists.collect { exists ->
                    if (exists == false) {
                        weatherInfo?.let { weatherViewModel.saveLocationToFavorites(it) }
                        binding.markFavorite.apply {
                            show()
                            setImageResource(R.drawable.baseline_favorite_24)
                            imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.white))
                        }
                        toast(getString(R.string.add_to_favorites_success_message))
                    } else toast(getString(R.string.already_exists_in_favorites_message))
                }
            }
            weatherViewModel.isLocationAlreadyExists(location!!)
        }
    }

    private fun goToFavorites() {
        startActivity { Intent(this, FavoritePlacesActivity::class.java) }
    }

    private fun goToSearch() {
        startActivity { Intent(this, SearchPlacesActivity::class.java) }
    }

    private fun showLoading() {
        binding.loadingLayout.show()
    }

    private fun hideLoading() {
        binding.loadingLayout.gone()
    }


    companion object {
        private var weatherInfo: WeatherEntity? = null
    }

}




