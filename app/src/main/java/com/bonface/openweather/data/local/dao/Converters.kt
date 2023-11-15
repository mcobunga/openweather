package com.bonface.openweather.data.local.dao

import androidx.room.TypeConverter
import com.bonface.openweather.data.model.City
import com.bonface.openweather.data.model.Clouds
import com.bonface.openweather.data.model.Coord
import com.bonface.openweather.data.model.CurrentWeather
import com.bonface.openweather.data.model.DailyForecast
import com.bonface.openweather.data.model.Main
import com.bonface.openweather.data.model.Rain
import com.bonface.openweather.data.model.Snow
import com.bonface.openweather.data.model.Sys
import com.bonface.openweather.data.model.Weather
import com.bonface.openweather.data.model.WeatherForecast
import com.bonface.openweather.data.model.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringArray(value: Array<String>): String {
        val gson = Gson()
        val type = object : TypeToken<Array<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringArray(value: String): Array<String> {
        val gson = Gson()
        val type = object : TypeToken<Array<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromIntArray(value: Array<Int>): String {
        val gson = Gson()
        val type = object : TypeToken<Array<Int>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toIntArray(value: String): Array<Int> {
        val gson = Gson()
        val type = object : TypeToken<Array<Int>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCity(value: List<City>): String {
        val gson = Gson()
        val type = object : TypeToken<List<City>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCity(value: String): List<City> {
        val gson = Gson()
        val type = object : TypeToken<List<City>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromClouds(value: Clouds?): String? {
        val gson = Gson()
        val type = object : TypeToken<Clouds>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toClouds(value: String?): Clouds? {
        val gson = Gson()
        val type = object : TypeToken<Clouds>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCoord(value: Coord?): String? {
        val gson = Gson()
        val type = object : TypeToken<Coord>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCoord(value: String?): Coord? {
        val gson = Gson()
        val type = object : TypeToken<Coord>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromForecast(value: DailyForecast?): String? {
        val gson = Gson()
        val type = object : TypeToken<DailyForecast>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toForecast(value: String?): DailyForecast? {
        val gson = Gson()
        val type = object : TypeToken<DailyForecast>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMain(value: Main?): String? {
        val gson = Gson()
        val type = object : TypeToken<Main>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMain(value: String?): Main? {
        val gson = Gson()
        val type = object : TypeToken<Main>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromRain(value: List<Rain>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Rain>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toRain(value: String): List<Rain> {
        val gson = Gson()
        val type = object : TypeToken<List<Rain>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSnow(value: List<Snow>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Snow>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSnow(value: String): List<Snow> {
        val gson = Gson()
        val type = object : TypeToken<List<Snow>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSys(value: List<Sys>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Sys>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSys(value: String): List<Sys> {
        val gson = Gson()
        val type = object : TypeToken<List<Sys>>() {}.type
        return gson.fromJson(value, type)
    }


    @TypeConverter
    fun fromWeather(value: Weather?): String? {
        val gson = Gson()
        val type = object : TypeToken<Weather>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeather(value: String?): Weather? {
        val gson = Gson()
        val type = object : TypeToken<Weather>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromWind(value: List<Wind>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Wind>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toCurrentWeatherResponse(value: String): List<CurrentWeather> {
        val gson = Gson()
        val type = object : TypeToken<List<CurrentWeather>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromCurrentWeatherResponse(value: List<CurrentWeather>): String {
        val gson = Gson()
        val type = object : TypeToken<List<CurrentWeather>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeatherForecastResponse(value: String): List<WeatherForecast> {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherForecast>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromWeatherForecastResponse(value: List<WeatherForecast>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WeatherForecast>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWind(value: String): List<Wind> {
        val gson = Gson()
        val type = object : TypeToken<List<Wind>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}