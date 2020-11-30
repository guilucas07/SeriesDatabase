package com.guilhermelucas.model

data class Show(
    val id: Int,
    val name: String,
    val language: String?,
    val imageUrl: String?,
    val imageLargeUrl: String?,
    val officialSite: String?,
    val premiered: String?,
    val runtime: Int?,
    val status: String?,
    val summary: String?,
    val type: String?,
    val updated: Int?,
    val url: String?,
    val weight: Int?,
    val rating: Double?,
    val genres: List<String>? = null,
    val seasons: List<Season>? = null,
    val seriesSchedule: List<SeriesSchedule>? = null
)
