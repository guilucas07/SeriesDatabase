package com.guilhermelucas.seriesdatabase.seriesdetail

data class SeriesDetailViewObject(
    val name: String,
    val genres: String,
    val exhibitionDescription: String,
    val summary: String,
    val seasonsList: List<String>?
)
