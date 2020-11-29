package com.guilhermelucas.seriesdatabase.utils.extensions

import com.guilhermelucas.model.Show
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.seriesdetail.SeriesDetailViewObject
import com.guilhermelucas.model.ResourceProvider

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

//TODO
fun Show.toDetailsViewObject(resourceProvider: ResourceProvider): SeriesDetailViewObject {
    return SeriesDetailViewObject(
        name = name,
        genres = "",
        exhibitionDescription = "Terça as 16:00",
        summary = summary.orEmpty(),
        imageUrl = imageLargeUrl ?: imageUrl,
        seasonsList = seasons?.map {
            resourceProvider.getString(
                R.string.season_with_number,
                it.number
            )
        }
    )
}
