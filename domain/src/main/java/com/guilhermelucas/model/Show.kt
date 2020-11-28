package com.guilhermelucas.model

data class Show(
    val id: Int,
    val imageUrl : String?,
    val imageLargeUrl : String?,
    val language: String,
    val name: String,
    val officialSite: String?,
    val premiered: String?,
    val runtime: Int?,
    val status: String,
    val summary: String,
    val type: String,
    val updated: Int,
    val url: String,
    val weight: Int,
    val rating : Double?
)