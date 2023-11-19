package com.bonface.openweather.base

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bonface.openweather.data.local.OpenWeatherDatabase
import com.bonface.openweather.data.local.dao.CurrentWeatherDao
import com.bonface.openweather.data.local.dao.FavoritePlacesDao
import com.bonface.openweather.data.local.dao.WeatherForecastDao
import com.bonface.openweather.data.remote.OpenWeatherApi
import com.bonface.openweather.dispatcher.OpenWeatherDispatcher
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

open class OpenWeatherBaseTest  {

    lateinit var openWeatherApi: OpenWeatherApi
    private lateinit var database: OpenWeatherDatabase
    private lateinit var loggingInterceptor: HttpLoggingInterceptor
    lateinit var currentWeatherDao: CurrentWeatherDao
    lateinit var weatherForecastDao: WeatherForecastDao
    lateinit var favoritePlacesDao: FavoritePlacesDao
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var mockWebServer: MockWebServer


    @Before
    open fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, OpenWeatherDatabase::class.java)
            .setQueryExecutor(dispatcher.asExecutor())
            .setTransactionExecutor(dispatcher.asExecutor())
            .allowMainThreadQueries()
            .build()

        currentWeatherDao = database.currentWeatherDao()
        weatherForecastDao = database.weatherForecastDao()
        favoritePlacesDao = database.favoritePlacesDao()

        mockWebServer()
        setLoggingInterceptor()
        setRetrofitWithMoshi()
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
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
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


    @After
    fun closeDb() {
        database.close()
    }

    fun enqueueResponse(response: MockResponse) {
        mockWebServer.enqueue(response)
    }

}