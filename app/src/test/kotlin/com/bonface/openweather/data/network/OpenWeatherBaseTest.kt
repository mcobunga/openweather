package com.bonface.openweather.data.network

import com.bonface.openweather.data.remote.OpenWeatherApi
import com.bonface.openweather.dispatcher.OpenWeatherDispatcher
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

open class OpenWeatherBaseTest  {

    lateinit var openWeatherApi: OpenWeatherApi
    private lateinit var loggingInterceptor: HttpLoggingInterceptor
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var mockWebServer: MockWebServer

    @Before
    open fun before() {
        mockWebServer()
        setLoggingInterceptor()
        setRetrofitWithMoshi()
    }

    @After
    open fun after() {
        mockWebServer.shutdown()
    }

    private fun mockWebServer() {
        mockWebServer = MockWebServer().apply {
            dispatcher = OpenWeatherDispatcher()
            start()
        }
    }

    private fun setLoggingInterceptor() {
        loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        okHttpClient = buildOkhttpClient(loggingInterceptor)
    }

    private fun setRetrofitWithMoshi() {
        openWeatherApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(OpenWeatherApi::class.java)
    }

    private fun buildOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val moshi: Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

}