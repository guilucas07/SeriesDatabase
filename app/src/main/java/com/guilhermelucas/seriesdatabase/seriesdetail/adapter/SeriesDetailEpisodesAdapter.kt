package com.guilhermelucas.seriesdatabase.seriesdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.guilhermelucas.seriesdatabase.databinding.AdapterSeasonEpisodeDetailItemBinding
import com.guilhermelucas.seriesdatabase.databinding.AdapterSeasonEpisodeItemBinding
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeDetailsViewObject
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.model.EpisodeViewObject
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.viewholder.EpisodeDetailsViewHolder
import com.guilhermelucas.seriesdatabase.seriesdetail.adapter.viewholder.EpisodeViewHolder
import com.guilhermelucas.seriesdatabase.utils.ExpandableRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope

class SeriesDetailEpisodesAdapter(
    coroutineScope: CoroutineScope,
    private val items: ArrayList<EpisodeViewObject> = arrayListOf()
) :
    ExpandableRecyclerViewAdapter<EpisodeDetailsViewObject,
            EpisodeViewObject, EpisodeViewHolder, EpisodeDetailsViewHolder>(
        items, ExpandingDirection.VERTICAL, coroutineScope
    ) {

    fun loadItems(list: List<EpisodeViewObject>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateParentViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            AdapterSeasonEpisodeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindParentViewHolder(
        parentViewHolder: EpisodeViewHolder,
        expandableType: EpisodeViewObject,
        position: Int
    ) {
        parentViewHolder.bind(expandableType)
    }

    override fun onCreateChildViewHolder(
        child: ViewGroup,
        viewType: Int
    ): EpisodeDetailsViewHolder {
        return EpisodeDetailsViewHolder(
            AdapterSeasonEpisodeDetailItemBinding.inflate(
                LayoutInflater.from(child.context),
                child,
                false
            )
        )
    }

    override fun onBindChildViewHolder(
        childViewHolder: EpisodeDetailsViewHolder,
        expandedType: EpisodeDetailsViewObject,
        expandableType: EpisodeViewObject,
        position: Int
    ) {
        childViewHolder.bind(expandedType)
    }

    override fun onExpandableClick(
        expandableViewHolder: EpisodeViewHolder,
        expandableType: EpisodeViewObject
    ){
        //nothing to do
    }

    override fun onExpandedClick(
        expandableViewHolder: EpisodeViewHolder,
        expandedViewHolder: EpisodeDetailsViewHolder,
        expandedType: EpisodeDetailsViewObject,
        expandableType: EpisodeViewObject
    ){
        //nothing to do
    }
}
