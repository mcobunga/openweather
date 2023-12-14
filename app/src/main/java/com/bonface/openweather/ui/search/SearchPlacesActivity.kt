package com.bonface.openweather.ui.search

import android.app.SearchManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonface.openweather.BuildConfig.MAPS_API_KEY
import com.bonface.openweather.R
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.SearchResult
import com.bonface.openweather.databinding.ActivitySearchPlacesBinding
import com.bonface.openweather.ui.searchResult.SEARCHED_LOCATION_WEATHER_BOTTOM_SHEET
import com.bonface.openweather.ui.searchResult.SearchedLocationWeatherBottomSheet
import com.bonface.openweather.ui.viewmodel.WeatherViewModel
import com.bonface.openweather.utils.Resource
import com.bonface.openweather.utils.gone
import com.bonface.openweather.utils.hideKeyboard
import com.bonface.openweather.utils.onBackPressedCallback
import com.bonface.openweather.utils.show
import com.bonface.openweather.utils.showKeyboard
import com.bonface.openweather.utils.toast
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

@Suppress("DEPRECATION")
@AndroidEntryPoint
class SearchPlacesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchPlacesBinding
    private lateinit var placesClient: PlacesClient
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializePlaces()
        setTextChangeListener()
        customizeSearchView()
        setupSearchAdapter()
        setSearchingStatus()

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback())
    }

    private fun setTextChangeListener() {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        binding.discoverPlacesEditText.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isSubmitButtonEnabled = true
            setOnQueryTextListener(searchTextWatcher)
        }
    }

    private val searchTextWatcher = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(editable: String?): Boolean {
            searchAdapter.filter.filter(editable.toString())
            return false
        }
        override fun onQueryTextChange(editable: String?): Boolean {
            searchAdapter.filter.filter(editable.toString())
            return false
        }
    }

    private fun setSearchingStatus() {
        searchAdapter.isSearching.observe(this) { isSearching ->
            if (isSearching) {
                binding.emptyState.apply {
                    text = getString(R.string.searching)
                }
                showLoading()
            } else hideLoading()
        }
    }

    private fun setupSearchAdapter() {
        binding.searchedLocations.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
        searchAdapter.onSearchItemClicked {
            getLocationGeoCode(it)
            hideKeyboard(binding.discoverPlacesEditText)
        }
    }

    private fun initializePlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(this, MAPS_API_KEY)
        }
        placesClient = Places.createClient(this)
        val autocompleteSessionToken = AutocompleteSessionToken.newInstance()
        searchAdapter = SearchAdapter(autocompleteSessionToken, placesClient)
    }

    private fun getCurrentWeatherForSelectedLocation(location: Location, searchResult: SearchResult) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.currentWeather.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            hideLoading()
                            resource.data?.let { showBottomSheet(it) }
                        }
                        is Resource.Error -> {
                            hideLoading()
                            toast(resource.message.toString())
                        }
                        is Resource.Loading -> {
                            binding.emptyState.apply {
                                text = getString(R.string.fetching_weather, searchResult.primaryText)
                            }
                            showLoading()
                        }
                    }
                }
            }
        }
        weatherViewModel.getCurrentWeatherFromRemote(location)
    }

    private fun showBottomSheet(weather: CurrentWeather) {
        val bottomSheet = SearchedLocationWeatherBottomSheet(weather)
        if (!bottomSheet.isVisible) {
            bottomSheet.show(supportFragmentManager, SEARCHED_LOCATION_WEATHER_BOTTOM_SHEET)
        }
    }

    private fun getLocationGeoCode(searchResult: SearchResult) {
        try {
            val address = Geocoder(this).getFromLocationName("${searchResult.primaryText}, ${searchResult.secondaryText}", 1)?.firstOrNull()
            if (address != null) {
                val location = Location("This").apply {
                    latitude = address.latitude
                    longitude = address.longitude
                }
                getCurrentWeatherForSelectedLocation(location, searchResult)
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    private fun customizeSearchView() {
        binding.discoverPlacesEditText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    showKeyboard(this)
                } else hideKeyboard(this)
            }
        }
    }

    private fun showLoading() {
        binding.loadingLayout.show()
    }

    private fun hideLoading() {
        binding.loadingLayout.gone()
    }

}