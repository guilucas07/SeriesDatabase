package com.guilhermelucas.seriesdatabase.home.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.databinding.AdapterMovieItemBinding
import com.guilhermelucas.seriesdatabase.utils.extensions.loadImage

class MovieViewHolder(
    private val clickListener: (position: Int) -> Unit,
    private val layout: AdapterMovieItemBinding
) : RecyclerView.ViewHolder(layout.root) {

    fun bind(movie: AdapterItem) {
        with(layout) {
            textMovieTitle.text = movie.title

            movie.releaseDate?.run {
                textMovieReleaseDate.text = this
            }

            root.setOnClickListener {
                clickListener(adapterPosition)
            }

            imageMoviePoster.loadImage(movie.posterUrl)

            textMovieSimilarity.isVisible = movie.similarity != null
            if(movie.similarity != null){
                textMovieSimilarity.text = "Score ${movie.similarity.toInt()} %"
            }
        }
    }
}