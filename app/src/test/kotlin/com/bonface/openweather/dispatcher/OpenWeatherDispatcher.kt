package com.bonface.openweather.dispatcher

import com.google.common.io.Resources.getResource
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.File
import java.net.HttpURLConnection

class OpenWeatherDispatcher : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        return when {
            request.path?.contains("weather", true) == true -> {
                MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(createMockResponse("current_weather.json"))
            }
            request.path?.contains("onecall", true) == true -> {
                MockResponse().setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(createMockResponse("weather_forecast.json"))
            }
            else -> throw IllegalArgumentException("Path not found: ${request.path}")
        }
    }

    private fun createMockResponse(filePath: String) = String(File(getResource(filePath).path).readBytes())

}