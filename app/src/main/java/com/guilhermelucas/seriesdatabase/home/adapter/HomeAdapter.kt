package com.guilhermelucas.seriesdatabase.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.R

class HomeAdapter(
    private val clickListener: (position: Int) -> Unit
) :
    RecyclerView.Adapter<MovieViewHolder>() {

    private val watchlist: ArrayList<AdapterItem> =
        arrayListOf()

    fun loadItems(list: List<AdapterItem>) {
        watchlist.clear()
        watchlist.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_movie_item, parent, false)
        return MovieViewHolder(
            clickListener,
            view
        )
    }

    override fun getItemCount() = watchlist.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        watchlist.getOrNull(position)?.let {
            holder.bind(it)
        }
    }
}
