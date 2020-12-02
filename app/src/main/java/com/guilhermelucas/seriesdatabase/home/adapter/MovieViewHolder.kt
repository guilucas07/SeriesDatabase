package com.guilhermelucas.seriesdatabase.home.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.databinding.AdapterMovieItemBinding
import com.guilhermelucas.seriesdatabase.utils.extensions.loadImage

class MovieViewHolder(
    private val clickListener: (position: Int) -> Unit,
    private val layout: AdapterMovieItemBinding
) : RecyclerView.ViewHolder(layout.root) {

    fun bind(series: AdapterItem) {
        with(layout) {
            titleText.text = series.title
            ratingText.text = series.ratingText
            posterImage.loadImage(series.posterUrl)
            textMovieSimilarity.isVisible = series.similarity != null
            textMovieSimilarity.text = series.similarity
            root.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }
}