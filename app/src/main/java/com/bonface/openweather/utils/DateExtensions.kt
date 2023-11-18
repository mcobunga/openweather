package com.bonface.openweather.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun getDay(value: Long): String? {
    val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
    return value.let { dt ->
        dt.let { sdf.format(Date(it * 1000)) }
    }
}

fun lastUpdated(updated: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return run {
        sdf.format(Date(updated))
    }
}