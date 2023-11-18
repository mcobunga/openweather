package com.bonface.openweather.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.bonface.openweather.R
import com.google.android.material.snackbar.Snackbar

fun Activity.startActivity(intent: () -> Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_from_right, R.anim.slide_to_left)
        startActivity(intent(), options.toBundle())
    } else {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}

fun Fragment.snackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, message, duration).show()
}

fun Activity.updateStatusBarColor(resource: Int) {
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