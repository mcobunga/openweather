package com.bonface.openweather.utils

sealed class NetworkResponse<out T> {
    data class Success<out T>(val value: T) : NetworkResponse<T>()
    data class Failure(val errorHolder: ErrorHolder) : NetworkResponse<Nothing>()
}

data class ErrorHolder(override val message: String, val statusCode: Int?, val body: String? = null) : Exception(message)