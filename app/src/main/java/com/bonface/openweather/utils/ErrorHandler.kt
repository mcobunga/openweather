package com.bonface.openweather.utils

import com.bonface.openweather.OpenWeather.Companion.getAppContext
import com.bonface.openweather.R
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
            is UnknownHostException -> MobileException(getAppContext().getString(R.string.no_internet_connection), 1000)
            is SocketTimeoutException -> MobileException(getAppContext().getString(R.string.no_internet_connection), 3000)
            is SSLHandshakeException -> MobileException(getAppContext().getString(R.string.app_update_needed), 4000)
            is ConnectException -> MobileException(getAppContext().getString(R.string.unable_to_connect), 5000)
            is HttpException -> getServerSideExceptions(exception)
            is IOException -> MobileException(getAppContext().getString(R.string.problem_processing_request), 2000)
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
                500 -> getAppContext().getString(R.string.internal_server_error)
                503 -> getAppContext().getString(R.string.service_unavailable)
                else -> getAppContext().getString(R.string.no_server_response)
            }
        }

        val errorCode = e.response()?.code()
        return MobileException(message.toString(), errorCode?: -1)
    }

}