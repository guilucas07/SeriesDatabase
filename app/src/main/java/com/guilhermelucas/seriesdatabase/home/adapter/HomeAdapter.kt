package com.guilhermelucas.seriesdatabase.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.databinding.AdapterMovieItemBinding

class HomeAdapter(
    private val clickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<SeriesViewHolder>() {

    private val items: ArrayList<AdapterItem> =
        arrayListOf()

    fun loadItems(list: List<AdapterItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(
            clickListener,
            AdapterMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it)
        }
    }
}
