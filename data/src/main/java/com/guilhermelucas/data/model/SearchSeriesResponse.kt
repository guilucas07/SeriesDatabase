package com.guilhermelucas.data.model

import com.squareup.moshi.Json

data class SearchSeriesResponse(
    @field:Json(name = "score") val score: Double,
    @field:Json(name = "show") val series : SeriesResponse
)
