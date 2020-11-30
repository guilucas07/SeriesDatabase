package com.guilhermelucas.data.model

data class SeasonResponse(
    val _links: Links,
    val endDate: String?,
    val episodeOrder: Int?,
    val id: Int,
    val image: Image?,
    val name: String?,
    val network: Network?,
    val number: Int,
    val premiereDate: String?,
    val summary: String?,
    val url: String?,
    val webChannel: WebChannel?
)
