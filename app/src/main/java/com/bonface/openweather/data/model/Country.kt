package com.bonface.openweather.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Country(
    @Json(name = "matchLevel") val matchLevel: String? = null,
    @Json(name = "value") val value: String? = null,
    @Json(name = "matchedWords") val matchedWords: List<Any?>? = null
)
