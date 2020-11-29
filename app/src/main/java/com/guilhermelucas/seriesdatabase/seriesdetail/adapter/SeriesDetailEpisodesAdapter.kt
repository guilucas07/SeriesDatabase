package com.guilhermelucas.seriesdatabase.seriesdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.databinding.AdapterMovieItemBinding
import com.guilhermelucas.seriesdatabase.databinding.AdapterSeasonEpisodeItemBinding
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeSeriesDetailsViewObject

class SeriesDetailEpisodesAdapter(
    private val clickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<EpisodesViewHolder>() {

    private val items: ArrayList<EpisodeSeriesDetailsViewObject> =
        arrayListOf()

    fun loadItems(list: List<EpisodeSeriesDetailsViewObject>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(
            clickListener,
            AdapterSeasonEpisodeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it)
        }
    }
}
