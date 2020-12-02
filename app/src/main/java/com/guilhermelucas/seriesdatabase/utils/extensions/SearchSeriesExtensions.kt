package com.guilhermelucas.seriesdatabase.utils.extensions

import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.model.SearchSeries
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem

fun SearchSeries.toAdapterItem(resourceProvider: ResourceProvider): AdapterItem {
    return series.toAdapterItem(resourceProvider)
        .copy(similarity = resourceProvider.getString(R.string.similarity_score, searchSimilarity))
}
