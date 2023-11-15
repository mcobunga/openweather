package com.bonface.openweather.data.remote

import com.bonface.openweather.utils.ErrorHolder
import com.bonface.openweather.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

abstract class BaseRepository {
    suspend fun <T> apiCall(apiCall: suspend () -> T): NetworkResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(apiCall.invoke())
            } catch (exception: Exception) {
                exception.printStackTrace()
                when (exception) {
                    is UnknownHostException -> NetworkResponse.Failure(ErrorHolder("No internet connection, try again", 1000))
                    is SocketTimeoutException -> NetworkResponse.Failure(ErrorHolder("There was a problem processing your request. Please try again", 3000))
                    is SSLHandshakeException -> NetworkResponse.Failure(ErrorHolder("You need to update the app and try again", 4000))
                    is ConnectException -> NetworkResponse.Failure(ErrorHolder("There was a problem processing your request. Please try again", 5000))
                    is HttpException -> NetworkResponse.Failure(extractHttpExceptions(exception))
                    is IOException -> NetworkResponse.Failure(ErrorHolder("Unable to connect, check your connection", 2000))
                    else -> NetworkResponse.Failure(ErrorHolder(exception.message.toString(), 1))
                }
            }
        }
    }

    private fun extractHttpExceptions(e: HttpException): ErrorHolder {
        val body = e.response()?.errorBody()
        val jsonString = body?.string()
        val message = try {
            val jsonObject = jsonString?.let { JSONObject(it) }
            jsonObject?.getString("message")
        } catch (exception: JSONException) {
            when (e.code()) {
                500 -> "Internal server error, try again later."
                503 -> "Service temporarily unavailable, try again later"
                else -> "There was a problem completing your request. Try again later."
            }
        }

        val errorCode = e.response()?.code()
        return ErrorHolder(message.toString(), errorCode, jsonString!!)
    }
}