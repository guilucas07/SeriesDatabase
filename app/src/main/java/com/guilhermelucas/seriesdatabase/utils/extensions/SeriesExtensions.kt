package com.guilhermelucas.seriesdatabase.utils.extensions

import android.os.Build
import com.guilhermelucas.model.Series
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.detail.model.SeriesDetailViewObject
import com.guilhermelucas.model.ResourceProvider
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

fun Series.toAdapterItem(resourceProvider: ResourceProvider): AdapterItem {

    return AdapterItem(
        id,
        name,
        summary,
        rating,
        imageUrl.orEmpty(),
        imageLargeUrl.orEmpty(),
        premiered,
        ratingText = rating?.let { resourceProvider.getString(R.string.series_rating, it) } ?: resourceProvider.getString(R.string.series_without_rating)
    )
}

fun Series.toDetailsViewObject(resourceProvider: ResourceProvider): SeriesDetailViewObject {

    var exhibitionDescription = seriesSchedule?.daysOfWeek?.joinToString {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            it.getDisplayName(TextStyle.SHORT, Locale.US).capitalize(Locale.US)
        } else {
            it.name.capitalize(Locale.US)
        }
    }.orEmpty()

    if (seriesSchedule?.time != null)
        exhibitionDescription = resourceProvider.getString(
            R.string.series_exhibition_description,
            exhibitionDescription,
            seriesSchedule?.time.orEmpty()
        )

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
