package com.guilhermelucas.data.utils

import com.guilhermelucas.data.model.SeasonResponse
import com.guilhermelucas.model.Season

fun SeasonResponse.toSeason(): Season {
    return Season(
        id = id,
        number = number,
        totalEpisodes = episodeOrder
    )
}
