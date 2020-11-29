package com.guilhermelucas.data.utils

import androidx.core.net.toUri
import com.guilhermelucas.data.model.ShowResponse
import com.guilhermelucas.model.Show

fun ShowResponse.toShow(): Show {
    fun String.forceHttps() : String = toUri().buildUpon().scheme("https").toString()
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
        weight = weight
    )
}
