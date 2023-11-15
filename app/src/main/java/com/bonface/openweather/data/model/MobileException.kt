package com.bonface.openweather.data.model

class MobileException(
    message: String,
    val statusCode: Int = -1
) : Exception(message)