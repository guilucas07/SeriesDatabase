package com.guilhermelucas.seriesdatabase.utils.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.guilhermelucas.model.Show
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.seriesdetail.SeriesDetailViewObject
import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.model.SeriesSchedule
import com.guilhermelucas.seriesdatabase.BuildConfig
import java.time.format.TextStyle
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


fun Show.toDetailsViewObject(resourceProvider: ResourceProvider): SeriesDetailViewObject {
    val exhibitionDescription =
        seriesSchedule?.map {
            val displayName =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                } else {
                    it.dayOfWeek.name.capitalize(Locale.getDefault())
                }
            resourceProvider.getString(R.string.series_exhibition_description, displayName, it.time)
        }?.joinToString { "${it}\n" }.orEmpty()

    return SeriesDetailViewObject(
        name = name,
        genres = genres?.joinToString(separator = " / ").orEmpty(),
        exhibitionDescription = exhibitionDescription,
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
