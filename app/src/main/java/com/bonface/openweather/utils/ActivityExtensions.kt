package com.bonface.openweather.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bonface.openweather.R
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode
import java.text.DecimalFormat

fun Activity.startActivity(intent: () -> Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_from_right, R.anim.slide_to_left)
        startActivity(intent(), options.toBundle())
    } else {
        startActivity(intent)
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }
}

fun AppCompatActivity.snackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, message, duration).show()
}

fun Fragment.snackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(view, message, duration).show()
}

fun roundOffDecimal(number: Double): Double {
    val df = DecimalFormat("#.###")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}