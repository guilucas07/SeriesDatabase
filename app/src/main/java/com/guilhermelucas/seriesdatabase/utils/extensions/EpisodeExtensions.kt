package com.guilhermelucas.seriesdatabase.utils.extensions

import com.guilhermelucas.model.Episode
import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeDetailsViewObject
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeViewObject
import java.text.SimpleDateFormat
import java.util.*

fun Episode.toEpisodeViewObject(resourceProvider: ResourceProvider): EpisodeViewObject {
    return EpisodeViewObject(
        id = id,
        number = number?.let { it.toString() } ?: " - ",
        name = name.orEmpty(),
        duration = durationMinutes?.let {
            resourceProvider.getString(
                R.string.episode_duration_minutes,
                it
            )
        }.orEmpty(),
        details = toEpisodeDetails(resourceProvider)
    )
}

private fun Episode.toEpisodeDetails(resourceProvider: ResourceProvider): EpisodeDetailsViewObject {
    val formattedDate = airDate?.let {
        SimpleDateFormat(
            resourceProvider.getString(R.string.date_format),
            Locale.US
        ).format(it)
    }

    return EpisodeDetailsViewObject(
        id = id,
        season = season,
        summary = summary ?: resourceProvider.getString(R.string.episode_without_summary),
        image = imageUrl,
        airDate = formattedDate
    )
}
