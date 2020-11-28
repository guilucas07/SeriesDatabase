package com.guilhermelucas.seriesdatabase.utils.extensions

import com.guilhermelucas.model.Show
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import java.text.SimpleDateFormat
import java.util.*

fun Show.toAdapterItem(): AdapterItem {

    return AdapterItem(
        id,
        name,
        summary,
        rating,
        imageUrl.orEmpty(),
        imageLargeUrl.orEmpty(),
        premiered
    )
}
