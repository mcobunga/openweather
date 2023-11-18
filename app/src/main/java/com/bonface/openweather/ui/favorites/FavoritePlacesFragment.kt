package com.bonface.openweather.ui.favorites

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonface.openweather.R
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.databinding.FragmentFavoritePlacesBinding
import com.bonface.openweather.ui.favorites.adapter.PlacesListAdapter
import com.bonface.openweather.ui.viewmodel.WeatherViewModel
import com.bonface.openweather.utils.gone
import com.bonface.openweather.utils.isAccessFineLocationGranted
import com.bonface.openweather.utils.isLocationEnabled
import com.bonface.openweather.utils.roundOffDecimal
import com.bonface.openweather.utils.show
import com.bonface.openweather.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

@AndroidEntryPoint
class FavoritePlacesFragment : Fragment() {
    private var _binding: FragmentFavoritePlacesBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var placesListAdapter: PlacesListAdapter
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritePlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        when {
            requireContext().isAccessFineLocationGranted() -> {
                when {
                    requireContext().isLocationEnabled() -> {
                        setLocationObserver()
                        weatherViewModel.getCurrentLocation() //Will check fetched locations if any matches current location
                    }
                    else -> getFavoritePlaces() //Fetching for places without the need to show current location
                }
            }
            else -> getFavoritePlaces() //Fetching for places without the need to show current location
        }
    }

    private fun setLocationObserver() {
        weatherViewModel.currentLocation.observe(viewLifecycleOwner) {
            getFavoritePlaces(it)
        }
    }

    private fun getFavoritePlaces(location: Location? = null) {
        showLoading()
        lifecycleScope.launch {
            weatherViewModel.getFavoritePlaces().collect { places ->
                setAdapterData(location, places)
            }
        }
    }

    private fun setAdapterData(location: Location?, places: List<FavoritePlacesEntity>) {
        if (places.isNotEmpty()) {
            places.forEach {
                if (location != null && (roundOffDecimal(location.latitude) == it.latitude && roundOffDecimal(location.longitude) == it.longitude)) {
                    it.isCurrentLocation = true
                }
            }
            for (i in 0 until places.toMutableList().size) {
                if (places[i].isCurrentLocation == true) {
                    Collections.rotate(places, i)
                }
            }
            placesListAdapter.differ.submitList(places)
            hideLoading()
        } else snackbar(binding.favoritesLayout, getString(R.string.empty_favorites_message))
    }

    private fun setupAdapter() {
        with(binding) {
            favoritePlacesRv.apply {
                adapter = placesListAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun showLoading() {
        binding.loadingLayout.show()
    }

    private fun hideLoading() {
        binding.loadingLayout.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}