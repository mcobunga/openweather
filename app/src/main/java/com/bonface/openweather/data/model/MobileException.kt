package com.bonface.openweather.data.model

import androidx.annotation.Keep

@Keep
class MobileException(
    message: String,
    val statusCode: Int = -1
) : Exception(message)