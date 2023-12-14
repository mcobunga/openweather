package com.bonface.openweather.data.model

import androidx.annotation.Keep

@Keep
data class SearchResult(
    val primaryText: String,
    val secondaryText: String
)
