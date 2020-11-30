package com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model

data class EpisodeDetailsViewObject(
    val id: Int,
    val image: String?,
    val summary: String,
    val season: Int?,
    val airDate: String?
)
