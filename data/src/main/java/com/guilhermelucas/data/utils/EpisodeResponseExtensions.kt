package com.guilhermelucas.data.utils

import androidx.core.net.toUri
import com.guilhermelucas.data.model.EpisodeResponse
import com.guilhermelucas.model.Episode
import java.text.SimpleDateFormat
import java.util.*

fun EpisodeResponse.toEpisode(): Episode {
    fun String.forceHttps(): String = toUri().buildUpon().scheme("https").toString()
    val dateFormatApi = "yyyy-MM-ddd"
    val date = airdate?.let { SimpleDateFormat(dateFormatApi, Locale.getDefault()).parse(it) }
    return Episode(
        id = id,
        summary = summary?.removeHtmlTags(),
        name = name,
        season = season,
        number = number,
        imageUrl = image?.medium?.forceHttps() ?: image?.original?.forceHttps(),
        durationMinutes = runtime,
        airDate = date
    )
}
