package com.guilhermelucas.seriesdatabase.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.utils.extensions.loadImage
import kotlinx.android.synthetic.main.adapter_movie_item.view.*

class MovieViewHolder(val clickListener: (position: Int) -> Unit, itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(movie: AdapterItem) {

        itemView.textMovieTitle.text = movie.title

        movie.releaseDate?.run {
            itemView.textMovieReleaseDate.text = this
        }

        itemView.setOnClickListener {
            clickListener(adapterPosition)
        }

        itemView.imageMoviePoster.loadImage(movie.posterUrl)
    }
}