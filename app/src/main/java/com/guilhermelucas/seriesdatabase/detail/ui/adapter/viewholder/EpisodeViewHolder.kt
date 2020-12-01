package com.guilhermelucas.seriesdatabase.detail.ui.adapter.viewholder

import com.guilhermelucas.seriesdatabase.databinding.AdapterSeasonEpisodeItemBinding
import com.guilhermelucas.seriesdatabase.detail.ui.adapter.model.EpisodeViewObject
import com.guilhermelucas.seriesdatabase.utils.ExpandableRecyclerViewAdapter

class EpisodeViewHolder(
    private val layout: AdapterSeasonEpisodeItemBinding
) : ExpandableRecyclerViewAdapter.ExpandableViewHolder(layout.root) {

    fun bind(episode: EpisodeViewObject) {
        with(layout) {
            episodeNumberText.text = episode.number

            titleText.text = episode.name

            durationText.text = episode.duration
        }
    }
}
