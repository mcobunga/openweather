package com.bonface.openweather.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

sealed class ApiResponse<out T> {
    data class Success<out T>(val value: T) : ApiResponse<T>()
    data class Failure(val errorHolder: Error) : ApiResponse<Nothing>()
}

data class Error(override val message: String, val statusCode: Int?, val body: String) : Exception(message)