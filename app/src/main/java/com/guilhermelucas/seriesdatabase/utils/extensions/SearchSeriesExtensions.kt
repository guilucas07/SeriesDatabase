package com.guilhermelucas.seriesdatabase.utils.extensions

import com.guilhermelucas.model.SearchSeries
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem

fun SearchSeries.toAdapterItem(): AdapterItem {
    return series.toAdapterItem().copy(similarity = searchSimilarity)
}
