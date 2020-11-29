package com.guilhermelucas.seriesdatabase.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.databinding.AdapterMovieItemBinding

class HomeAdapter(
    private val clickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<MovieViewHolder>() {

    private val items: ArrayList<AdapterItem> =
        arrayListOf()

    fun loadItems(list: List<AdapterItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            clickListener,
            AdapterMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            holder.bind(it)
        }
    }
}
