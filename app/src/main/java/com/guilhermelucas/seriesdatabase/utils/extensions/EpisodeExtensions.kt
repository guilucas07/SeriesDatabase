package com.guilhermelucas.seriesdatabase.utils.extensions

import com.guilhermelucas.model.Episode
import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeSeriesDetailsViewObject

fun Episode.toSeriesDetailAdapter(resourceProvider: ResourceProvider): EpisodeSeriesDetailsViewObject {
    return EpisodeSeriesDetailsViewObject(
        id = id,
        number = number?.let { it.toString() } ?: " - ",
        name = name.orEmpty(),
        duration = durationMinutes?.let {
            resourceProvider.getString(
                R.string.episode_duration_minutes,
                it
            )
        }.orEmpty()
    )
}
