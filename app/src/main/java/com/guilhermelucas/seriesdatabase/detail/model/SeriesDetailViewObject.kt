package com.guilhermelucas.seriesdatabase.detail.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SeriesDetailViewObject(
    val id: Int,
    val name: String,
    val genres: String,
    val exhibitionDescription: String,
    val summary: String,
    val imageUrl: String?,
    val seasonsList: List<String>?
) : Parcelable
