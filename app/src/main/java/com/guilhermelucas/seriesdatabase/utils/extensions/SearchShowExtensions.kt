package com.guilhermelucas.seriesdatabase.utils.extensions

import com.guilhermelucas.model.SearchShow
import com.guilhermelucas.model.Show
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import java.text.SimpleDateFormat
import java.util.*

fun SearchShow.toAdapterItem(): AdapterItem {
    return show.toAdapterItem().copy(similarity = searchSimilarity)
}
