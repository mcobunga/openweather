package com.bonface.openweather.ui.home

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonface.openweather.R
import com.bonface.openweather.data.local.entity.CurrentWeatherEntity
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.databinding.ActivityMainBinding
import com.bonface.openweather.ui.favorites.FavoritePlacesActivity
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.gone
import com.bonface.openweather.utils.isAccessFineLocationGranted
import com.bonface.openweather.utils.isLocationEnabled
import com.bonface.openweather.utils.show
import com.bonface.openweather.utils.showEnableGPSDialog
import com.bonface.openweather.utils.startActivity
import com.bonface.openweather.utils.toast
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
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
    private var isAllFabsVisible: Boolean? = false
    private var isWeatherInfoLoaded: Boolean? = false

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
        checkLocationPermission()
        setLocationObserver()
        setFloatingButtonController()
        getCachedWeatherInfo()
    }

    override fun onResume() {
        super.onResume()
        isAllFabsVisible = false
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
                goToFavorites()
            }
            searchPlaces.setOnClickListener {
                goToSearch()
            }
            addFavorite.setOnClickListener {
                saveToFavoriteLocations()
            }
        }
    }

    private fun checkLocationPermission() {
        when {
            isAccessFineLocationGranted() -> {
                when {
                    isLocationEnabled() -> mainViewModel.getCurrentLocation()
                    else -> showEnableGPSDialog()
                }
            }
            else -> requestAccessFineLocationPermission()
        }
    }


    private fun setLocationObserver() {
        mainViewModel.currentLocation.observe(this@MainActivity) {
            location = it
            it?.let { location ->
                getCurrentWeatherFromRemote(location)
                getWeatherForecastFromRemote(location)
            }
        }
    }


    private fun getCachedWeatherInfo() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.getCurrentWeatherFromDb().collect { currentWeather ->
                if (currentWeather.isNotEmpty()) {
                    isWeatherInfoLoaded = true
                    updateWeatherViews(currentWeather)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.getWeatherForecastFromDb().collect { dailyForecast ->
                if (dailyForecast.isNotEmpty()) {
                    forecastAdapter.differ.submitList(dailyForecast.take(5))
                }
            }
        }
    }

    private fun getCurrentWeatherFromRemote(location: Location) {
        mainViewModel.currentWeather.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    hideLoading()
                    resource.data?.let { mainViewModel.saveCurrentWeatherToDb(it) }
                    currentWeather = resource.data
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
        mainViewModel.getCurrentWeatherFromRemote(location)
    }

    private fun getWeatherForecastFromRemote(location: Location) {
        mainViewModel.forecastWeather.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> resource.data?.let { forecast ->
                    mainViewModel.saveWeatherForecastToDb(forecast)
                }
                is Resource.Error -> showSnackbarErrorMessage(resource.message.toString())
                is Resource.Loading ->{} //nothing - applying progressbar to only current weather api call
            }
        }
        mainViewModel.getWeatherForecastFromRemote(location)
    }

    private fun updateWeatherViews(weather: List<CurrentWeatherEntity>) {
        with(binding) {
            weather.firstOrNull().apply {

                this?.getCurrentWeatherBackgroundColor()
                    ?.let { ContextCompat.getColor(this@MainActivity, it) }
                    ?.let { weatherLayout.setBackgroundColor(it) }

                currentWeatherLayout.background = this?.getCurrentWeatherImage()
                    ?.let { ContextCompat.getDrawable(this@MainActivity, it) }
                currentLocation.text = getString(R.string.user_location, this?.name.toString(), this?.country.toString())
                currentTemp.text = this?.getTemperature()
                currentWeather.text = this?.weatherMain
                lastUpdatedAt.text = getString(R.string.last_updated, this?.lastUpdated())
                minTemperatureValue.text = this?.getMinTemperature()
                currentTemperatureValue.text = this?.getTemperature()
                maxTemperatureValue.text = this?.getMaxTemperature()
                this?.getCurrentWeatherImage()?.let { updateStatusBarColor(it) }
            }
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
            mainViewModel.getCurrentWeatherFromRemote(location!!)
            mainViewModel.getWeatherForecastFromRemote(location!!)
        } else {
            toast("Enable location permissions")
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
        with(binding) {
            fab.shrink()
            fab.setOnClickListener {
                isAllFabsVisible = if (!isAllFabsVisible!!) {
                    favoritePlaces.show()
                    if (isWeatherInfoLoaded == true) addFavorite.show() else addFavorite.hide()
                    if (isWeatherInfoLoaded == true) addFavActionText.show() else addFavActionText.gone()
                    searchPlaces.show()
                    favoritesActionText.show()
                    searchActionText.show()
                    fab.extend()
                    true
                } else {
                    favoritePlaces.hide()
                    addFavorite.hide()
                    searchPlaces.hide()
                    favoritesActionText.gone()
                    addFavActionText.gone()
                    searchActionText.gone()
                    fab.shrink()
                    false
                }
            }
        }
    }

    private fun requestAccessFineLocationPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(object : PermissionListener {
                override fun onPermissionGranted(ermissionGrantedResponse: PermissionGrantedResponse) {
                    mainViewModel.getCurrentLocation()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    toast("You need to allow location permission")
                    finish()
                }

                override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken) {
                    showEnableGPSDialog()
                }

        }).check()
    }

    private fun saveToFavoriteLocations() {
        if (location != null) {
            mainViewModel.isExists.observe(this) { isExists ->
                if (!isExists) {
                    currentWeather?.let { mainViewModel.saveLocationToFavorites(it) }
                    toast("Successfully added to favorites")
                } else toast("Already exists in your favorites")
            }
        }
    }

    private fun goToFavorites() {
        startActivity { Intent(this, FavoritePlacesActivity::class.java) }
    }

    private fun goToSearch() {
        startActivity { Intent(this, FavoritePlacesActivity::class.java) }
    }

    private fun showLoading() {
        binding.loadingLayout.show()
    }

    private fun hideLoading() {
        binding.loadingLayout.gone()
    }

    companion object {
        private var currentWeather: CurrentWeather? = null
    }

}




