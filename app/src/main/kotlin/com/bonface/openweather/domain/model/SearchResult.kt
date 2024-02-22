package com.bonface.openweather.domain.model

import androidx.annotation.Keep

@Keep
data class SearchResult(
    val primaryText: String,
    val secondaryText: String
)
