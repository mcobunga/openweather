package com.bonface.openweather.ui.favorites.placesMap

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bonface.openweather.R
import com.bonface.openweather.data.local.entity.FavoritePlacesEntity
import com.bonface.openweather.databinding.FragmentFavoritesMapsBinding
import com.bonface.openweather.ui.viewmodel.WeatherViewModel
import com.bonface.openweather.utils.bitmapDescriptorFromVector
import com.bonface.openweather.utils.isAccessFineLocationGranted
import com.bonface.openweather.utils.isLocationEnabled
import com.bonface.openweather.utils.roundOffLatLonDecimal
import com.bonface.openweather.utils.showEnableGPSDialog
import com.bonface.openweather.utils.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class FavoritesMapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private var _binding: FragmentFavoritesMapsBinding? = null
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var mapFragment: SupportMapFragment
    private var mMap: GoogleMap? = null
    private var currentLocation: Location? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()
        setLocationObserver()
    }

    private fun initializeMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.places_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.setOnMarkerClickListener(this)
        checkLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun setLocationObserver() {
        weatherViewModel.currentLocation.observe(viewLifecycleOwner) {
            currentLocation = it
            mMap?.isMyLocationEnabled = true
            mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            getFavoritePlaces(it)
        }
    }

    private fun getFavoritePlaces(location: Location) {
        lifecycleScope.launch {
            weatherViewModel.getFavoritePlaces().collect { favorites ->
                val currentLatLng = LatLng(roundOffLatLonDecimal(location.latitude), roundOffLatLonDecimal(location.longitude))
                favoriteLocations.clear()
                favoriteLocations.add(currentLatLng)
                setLocations(favorites)
            }
        }
    }

    private fun setLocations(favorites: List<FavoritePlacesEntity>) {
        val locations = favorites.filter { (it.latitude == currentLocation?.latitude && it.longitude == currentLocation?.longitude).not() }
        locations.forEach {
            favoriteLocations.add(LatLng(it.latitude!!, it.longitude!!))
        }
        placeCurrentLocationMarkerOnMap()
    }

    private fun checkLocationPermission() {
        when {
            requireContext().isAccessFineLocationGranted() -> {
                when {
                    requireContext().isLocationEnabled() -> weatherViewModel.getCurrentLocation()
                    else -> requireContext().showEnableGPSDialog()
                }
            }
            else -> requestAccessFineLocationPermission()
        }
    }

    private fun placeCurrentLocationMarkerOnMap() {
        val markerList = mutableListOf<MarkerOptions>()
        val builder = LatLngBounds.Builder()
        markerList.clear()
        val favorites = favoriteLocations.distinct()
        for (i in favorites.indices) {
            val titleStr = getAddress(favorites[i])
            val marker = MarkerOptions()
                .position(favorites[i])
                .title(titleStr)
                .icon(bitmapDescriptorFromVector(requireContext(),
                    if(favorites[i].latitude == roundOffLatLonDecimal(currentLocation!!.latitude) && favorites[i].longitude == roundOffLatLonDecimal(currentLocation!!.longitude))
                    R.drawable.current_location_marker else R.drawable.favorite_places_marker)
                )
            mMap?.addMarker(marker)
            markerList.add(marker)
        }
        for (marker in markerList) {
            builder.include(marker.position)
        }
        val bounds = builder.build()
        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30))
        if (favorites.size == 1) mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(favorites.first(), 12f))
    }

    @Suppress("DEPRECATION")
    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(requireContext())
        var addressText = ""
        try {
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.firstOrNull()
            if (address != null) {
                addressText = address.getAddressLine(0)
            }
        } catch (e: IOException) {
            Timber.e("Location Address", e.localizedMessage)
        }
        return addressText
    }

    private fun requestAccessFineLocationPermission() {
        Dexter.withContext(requireContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(object :
            PermissionListener {
            override fun onPermissionGranted(ermissionGrantedResponse: PermissionGrantedResponse) {
                weatherViewModel.getCurrentLocation()
            }

            override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                requireContext().toast("You need to allow location permission")
            }

            override fun onPermissionRationaleShouldBeShown(permissionRequest: PermissionRequest, permissionToken: PermissionToken) {
                requireContext().showEnableGPSDialog()
            }

        }).check()
    }

    override fun onMarkerClick(p0: Marker): Boolean = false

    companion object {
        private val favoriteLocations: MutableList<LatLng> = ArrayList()
    }

}
