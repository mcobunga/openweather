package com.bonface.openweather.utils

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationProvider @Inject constructor(private val fusedLocationProviderClient: FusedLocationProviderClient) {

    @SuppressLint("MissingPermission")
    fun getLocation(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = 1000L
            fastestInterval = 1000L
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                location?.let { trySend(it) }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
    }

}