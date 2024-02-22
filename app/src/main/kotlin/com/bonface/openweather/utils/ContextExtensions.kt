package com.bonface.openweather.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.SystemClock
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import com.bonface.openweather.R

fun Context.isLocationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)

}

fun Context.isAccessFineLocationGranted(): Boolean {
    return ActivityCompat.checkSelfPermission(applicationContext,  Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hideKeyboard(view: View) {
    if (view.isInEditMode) return
    view.post {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

fun Context.showKeyboard(view: View) {
    if (view.isInEditMode) return
    view.post {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!view.isFocused) view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
    view.postDelayed({
        val uptime = SystemClock.uptimeMillis()
        val width = view.width.toFloat()
        val height = view.height.toFloat()
        view.dispatchTouchEvent(
            MotionEvent.obtain(
                uptime,
                uptime,
                MotionEvent.ACTION_DOWN,
                width,
                height,
                0
            )
        )
        view.dispatchTouchEvent(
            MotionEvent.obtain(
                uptime,
                uptime,
                MotionEvent.ACTION_UP,
                width,
                height,
                0
            )
        )
    }, 200)
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showEnableGPSDialog() {
    AlertDialog.Builder(this)
        .setTitle(this.getString(R.string.enable_gps))
        .setMessage(this.getString(R.string.gps_desc))
        .setCancelable(false)
        .setPositiveButton(this.getString(R.string.enable_now)) { _, _ ->
            this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        .show()
}