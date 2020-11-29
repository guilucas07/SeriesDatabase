package com.guilhermelucas.data.utils

import com.guilhermelucas.data.model.EpisodeResponse
import com.guilhermelucas.model.Episode

fun EpisodeResponse.toEpisode(): Episode {
    return Episode(
        id = id,
        summary = summary,
        name = name,
        season = season,
        number = number,
        imageUrl = image?.medium ?: image?.original,
        durationMinutes = runtime
    )
}