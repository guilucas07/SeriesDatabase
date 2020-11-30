package com.guilhermelucas.data.model

import com.squareup.moshi.Json

data class ShowResponse(
    val _links: Links,
    val externals: Externals,
    val genres: List<String>,
    val id: Int,
    val image: Image?,
    val language: String?,
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
    val weight: Int?
)
