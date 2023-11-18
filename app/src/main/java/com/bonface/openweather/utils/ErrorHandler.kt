package com.bonface.openweather.utils

import com.bonface.openweather.data.model.MobileException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

object ErrorHandler {

    @Throws(IOException::class)
    fun handleException(exception: Exception): MobileException {
        exception.printStackTrace()
        return when (exception) {
            is UnknownHostException -> MobileException("No internet connection, try again", 1000)
            is SocketTimeoutException -> MobileException("There was a problem processing your request. Please try again", 3000)
            is SSLHandshakeException -> MobileException("You need to update the app and try again", 4000)
            is ConnectException -> MobileException("There was a problem processing your request. Please try again", 5000)
            is HttpException -> getServerSideExceptions(exception)
            is IOException -> MobileException("Unable to connect, check your connection", 2000)
            else -> MobileException(exception.message.toString(), 1)
        }
    }

    private fun getServerSideExceptions(e: HttpException): MobileException {
        val body = e.response()?.errorBody()
        val jsonString = body?.string()
        val message = try {
            val jsonObject = jsonString?.let { JSONObject(it) }
            jsonObject?.getString("message")
        } catch (jsonException: JSONException) {
            when (e.code()) {
                500 -> "Internal server error, try again later."
                503 -> "Service temporarily unavailable, try again later"
                else -> "There was a problem completing your request. Try again later."
            }
        }

        val errorCode = e.response()?.code()
        return MobileException(message.toString(), errorCode?: -1)
    }

}