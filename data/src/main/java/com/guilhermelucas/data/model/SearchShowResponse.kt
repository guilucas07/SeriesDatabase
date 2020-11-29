package com.guilhermelucas.data.model

import com.squareup.moshi.Json

data class SearchShowResponse(
    @Json(name = "score") val score: Double,
    @Json(name = "show") val show : ShowResponse
)
