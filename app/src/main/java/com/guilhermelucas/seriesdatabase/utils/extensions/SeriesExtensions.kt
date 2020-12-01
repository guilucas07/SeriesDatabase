package com.guilhermelucas.seriesdatabase.utils.extensions

import android.os.Build
import com.guilhermelucas.model.Series
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.detail.model.SeriesDetailViewObject
import com.guilhermelucas.model.ResourceProvider
import java.time.format.TextStyle
import java.util.*

fun Series.toAdapterItem(): AdapterItem {

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


fun Series.toDetailsViewObject(resourceProvider: ResourceProvider): SeriesDetailViewObject {
    val exhibitionDescription =
        seriesSchedule?.joinToString(separator = "\n") {
            val displayName =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
                } else {
                    it.dayOfWeek.name
                }

            resourceProvider.getString(
                R.string.series_exhibition_description,
                displayName.capitalize(Locale.US),
                it.time
            )
        }.orEmpty()

    return SeriesDetailViewObject(
        id = id,
        name = name,
        genres = genres?.joinToString(separator = " / ").orEmpty(),
        exhibitionDescription = exhibitionDescription,
        summary = summary ?: resourceProvider.getString(R.string.series_without_summary),
        imageUrl = imageLargeUrl ?: imageUrl,
        seasonsList = seasons?.map {
            resourceProvider.getString(
                R.string.season_with_number,
                it.number
            )
        }
    )
}
