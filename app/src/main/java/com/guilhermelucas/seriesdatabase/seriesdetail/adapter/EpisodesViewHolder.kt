package com.guilhermelucas.seriesdatabase.seriesdetail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.databinding.AdapterSeasonEpisodeItemBinding
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeSeriesDetailsViewObject

class EpisodesViewHolder(
    private val clickListener: (position: Int) -> Unit,
    private val layout: AdapterSeasonEpisodeItemBinding
) : RecyclerView.ViewHolder(layout.root) {

    fun bind(episode: EpisodeSeriesDetailsViewObject) {
        with(layout) {
            episodeNumberText.text = episode.number

            titleText.text = episode.name

            durationText.text = episode.duration

            root.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }
}