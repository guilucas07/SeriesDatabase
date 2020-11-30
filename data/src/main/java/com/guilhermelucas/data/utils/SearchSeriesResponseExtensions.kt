package com.guilhermelucas.data.utils

import com.guilhermelucas.data.model.SearchSeriesResponse
import com.guilhermelucas.model.SearchSeries

fun SearchSeriesResponse.toSearchSeries() =
    SearchSeries(score, series.toSeries())
