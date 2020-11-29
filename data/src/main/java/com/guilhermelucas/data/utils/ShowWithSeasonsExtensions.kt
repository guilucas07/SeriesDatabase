package com.guilhermelucas.data.utils

import androidx.core.net.toUri
import com.guilhermelucas.data.model.SeasonResponse
import com.guilhermelucas.data.model.ShowWithSeasons
import com.guilhermelucas.model.Season
import com.guilhermelucas.model.Show

fun ShowWithSeasons.toShow(): Show {
    fun String.forceHttps(): String = toUri().buildUpon().scheme("https").toString()
    return Show(
        id = id,
        imageUrl = image?.medium?.forceHttps(),
        imageLargeUrl = image?.original?.forceHttps(),
        name = name,
        language = language,
        officialSite = officialSite,
        premiered = premiered,
        rating = rating?.average,
        runtime = runtime,
        status = status,
        summary = summary,
        type = type,
        updated = updated,
        url = url,
        weight = weight,
        seasons = _embedded.seasons.map { it.toSeason() },
        genres = genres,
        seriesSchedule = schedule?.toSchedule()
    )
}

fun SeasonResponse.toSeason(): Season {
    return Season(
        id = id,
        number = number,
        totalEpisodes = episodeOrder
    )
}
