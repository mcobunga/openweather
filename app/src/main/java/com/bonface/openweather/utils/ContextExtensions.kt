package com.bonface.openweather.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.os.SystemClock
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.bonface.openweather.R
import com.bonface.openweather.utils.Constants.PERMISSION_REQUEST_CODE

fun Context.isLocationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)

}

fun Context.isAccessFineLocationGranted(): Boolean {
    return ActivityCompat.checkSelfPermission(applicationContext,  Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.getTintedDrawable(
    @DrawableRes drawableResId: Int,
    @ColorRes colorResId: Int
): Drawable? {
    if (drawableResId == 0) return null
    val drawable = getDrawableCompat(drawableResId).mutate()
    val color = getColorCompat(colorResId)
    drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    return drawable
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

fun Context.getDrawableCompat(@DrawableRes drawableId: Int): Drawable =
    ContextCompat.getDrawable(this, drawableId)!!

fun Context.getColorCompat(@ColorRes colorResId: Int): Int =
    ContextCompat.getColor(this, colorResId)

fun Context.getAppCompatDrawable(@DrawableRes resId: Int): Drawable? =
    AppCompatResources.getDrawable(this, resId)


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