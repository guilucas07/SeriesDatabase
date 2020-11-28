package com.guilhermelucas.seriesdatabase.home.adapter

data class AdapterItem(
    val id: Int,
    val title: String,
    val overview: String?,
    val voteAverage: Double?,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String?
)
