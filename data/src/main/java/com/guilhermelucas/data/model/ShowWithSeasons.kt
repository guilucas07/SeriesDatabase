package com.guilhermelucas.data.model

data class ShowWithSeasons(
    val _links: Links,
    val externals: Externals,
    val genres: List<String>,
    val id: Int,
    val image: Image?,
    val language: String,
    val name: String,
    val network: Network?,
    val officialSite: String?,
    val premiered: String?,
    val rating: Rating?,
    val runtime: Int?,
    val schedule: Schedule?,
    val status: String?,
    val summary: String?,
    val type: String?,
    val updated: Int?,
    val url: String?,
    val webChannel: WebChannel?,
    val weight: Int?,
    val _embedded: EmbeddedSeasonResponse
)

data class EmbeddedSeasonResponse(
    val seasons: List<SeasonResponse>
)