package com.guilhermelucas.data.utils

import androidx.core.net.toUri
import com.guilhermelucas.data.model.SeriesWithSeasons
import com.guilhermelucas.model.Series

fun SeriesWithSeasons.toSeries(): Series {
    fun String.forceHttps(): String = toUri().buildUpon().scheme("https").toString()
    return Series(
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
        summary = summary?.removeHtmlTags(),
        type = type,
        updated = updated,
        url = url,
        weight = weight,
        seasons = _embedded.seasons.map { it.toSeason() },
        genres = genres,
        seriesSchedule = schedule?.toSchedule()
    )
}
