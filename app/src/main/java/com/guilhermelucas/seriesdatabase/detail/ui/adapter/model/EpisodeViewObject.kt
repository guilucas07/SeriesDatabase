package com.guilhermelucas.seriesdatabase.detail.ui.adapter.model

import com.guilhermelucas.seriesdatabase.utils.ExpandableRecyclerViewAdapter

data class EpisodeViewObject(
    val id: Int,
    val number: String,
    val name: String,
    val duration: String,
    val details: EpisodeDetailsViewObject? = null,
) : ExpandableRecyclerViewAdapter.ExpandableGroup<EpisodeDetailsViewObject>() {
    override fun getExpandingItems(): List<EpisodeDetailsViewObject> {
        return if(details != null) listOf(details) else emptyList()
    }
}
