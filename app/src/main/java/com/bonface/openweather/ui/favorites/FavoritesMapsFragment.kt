package com.bonface.openweather.ui.favorites

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bonface.openweather.databinding.FragmentFavoritesMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class FavoritesMapsFragment : Fragment() {

    private var _binding: FragmentFavoritesMapsBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMap()

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(p0: LocationResult) {
//                super.onLocationResult(p0)
//                lastLocation = p0.lastLocation!!
//                favoriteLocations.add(LatLng(lastLocation.latitude, lastLocation.longitude))
//            }
//        }
//        createLocationRequest()
//        if (isLocationEnabled(requireContext())){
//            locationUpdateState = true
//            startLocationUpdates()
//        }

    }


    private fun initializeMap() {
//        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    /**
     * This callback is triggered when the map is ready to be used.
     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        googleMap = googleMap
//        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.setOnMarkerClickListener(this)
//        setUpMap()
//    }


    @SuppressLint("MissingPermission")
    private fun setUpMap() {
//        if (!isAccessFineLocationGranted(requireContext())) {
//            requestAccessFineLocationPermission(requireContext(), this)
//        }else{
//            googleMap?.isMyLocationEnabled = true
//            googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
//            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
//                if (location != null) {
//                    lastLocation = location
//                    val currentLatLng = LatLng(location.latitude, location.longitude)
//                    favoriteLocations.add(currentLatLng)
//                    placeMarkerOnMap()
//                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
//                }
//            }
//        }
    }

    private fun placeMarkerOnMap() {
        val markerList: List<MarkerOptions>
        val builder = LatLngBounds.Builder()
        markerList = ArrayList()
        markerList.removeAll(markerList)
        for (i in favoriteLocations.indices) {
            val titleStr = getAddress(favoriteLocations[i])
            val marker = MarkerOptions()
                .position(favoriteLocations[i])
                .title(titleStr)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            googleMap?.addMarker(marker)
            markerList.add(marker)
        }
        for (marker in markerList) {
            builder.include(marker.position)
        }
        val bounds = builder.build()
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30))
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
            Timber.e("MapsActivity", e.localizedMessage)
        }
        return addressText
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
//        if (!isAccessFineLocationGranted(requireContext())) {
//            requestAccessFineLocationPermission(requireContext(), activity = activity)
//        }else{
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//        }
    }

//    override fun onMarkerClick(p0: Marker): Boolean = false

    companion object{
        private val favoriteLocations: MutableList<LatLng> = ArrayList()
    }


}
