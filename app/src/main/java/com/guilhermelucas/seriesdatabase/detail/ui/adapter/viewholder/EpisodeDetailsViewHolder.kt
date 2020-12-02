package com.guilhermelucas.seriesdatabase.detail.ui.adapter.viewholder

import com.guilhermelucas.seriesdatabase.databinding.AdapterSeasonEpisodeDetailItemBinding
import com.guilhermelucas.seriesdatabase.detail.ui.adapter.model.EpisodeDetailsViewObject
import com.guilhermelucas.seriesdatabase.utils.ExpandableRecyclerViewAdapter
import com.guilhermelucas.seriesdatabase.utils.extensions.loadImage

class EpisodeDetailsViewHolder(
    private val layout: AdapterSeasonEpisodeDetailItemBinding
) : ExpandableRecyclerViewAdapter.ExpandedViewHolder(layout.root) {

    fun bind(episode: EpisodeDetailsViewObject) {
        with(layout) {
            posterImage.loadImage(episode.image)

            summaryText.text = episode.summary

            airDateText.text = episode.airDate
        }
    }
}
