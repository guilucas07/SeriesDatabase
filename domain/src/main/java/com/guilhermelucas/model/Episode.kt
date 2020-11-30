package com.guilhermelucas.model

import java.util.*

data class Episode(
    val id: Int,
    val imageUrl: String?,
    val name: String?,
    val number : Int?,
    val season : Int?,
    val durationMinutes: Int?,
    val summary: String?,
    val airDate : Date?,
)
