package com.bonface.openweather.ui.favorites

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
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
    private var locationsMap: GoogleMap? = null
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
        locationsMap = googleMap
        locationsMap?.uiSettings?.isZoomControlsEnabled = true
        locationsMap?.setOnMarkerClickListener(this)
        checkLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun setLocationObserver() {
        weatherViewModel.currentLocation.observe(viewLifecycleOwner) {
            currentLocation = it
            locationsMap?.isMyLocationEnabled = true
            locationsMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            getFavoritePlaces(it)
        }
    }

    private fun getFavoritePlaces(location: Location) {
        lifecycleScope.launch {
            weatherViewModel.getFavoritePlaces().collect { favorites ->
                val currentLatLng = LatLng(location.latitude, location.longitude)
                favoriteLocations.add(currentLatLng)
                setLocations(favorites)
                placeCurrentLocationMarkerOnMap()
            }
        }
    }

    private fun setLocations(favorites: List<FavoritePlacesEntity>) {
        val locations =
            favorites.filter { (it.latitude == currentLocation?.latitude && it.longitude == currentLocation?.longitude).not() }
        locations.forEach {
            favoriteLocations.add(LatLng(it.latitude!!, it.longitude!!))
        }
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
        val markerList: List<MarkerOptions>
        val builder = LatLngBounds.Builder()
        markerList = ArrayList()
        markerList.removeAll(markerList)
        for (i in 0 until favoriteLocations.size) {
            val titleStr = getAddress(favoriteLocations[i])
            val marker = MarkerOptions()
                .position(favoriteLocations[i])
                .title(titleStr)
                .icon(bitmapDescriptorFromVector(requireContext(),
                    if(favoriteLocations[i].latitude == currentLocation?.latitude && favoriteLocations[i].longitude == currentLocation?.longitude)
                    R.drawable.current_location_marker else R.drawable.favorite_places_marker)
                )
            locationsMap?.addMarker(marker)
            markerList.add(marker)
        }
        for (marker in markerList) {
            builder.include(marker.position)
        }
        val bounds = builder.build()
        locationsMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150,150,10))
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(requireContext())
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Timber.e("Maps Fragment", e.localizedMessage)
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
