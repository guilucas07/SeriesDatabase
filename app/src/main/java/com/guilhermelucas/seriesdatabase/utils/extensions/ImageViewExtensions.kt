package com.guilhermelucas.seriesdatabase.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.guilhermelucas.seriesdatabase.R

fun ImageView.loadUserImage(url: String?) =
    loadImage(url,
        requestOptionsUserImage
    )

fun ImageView.loadImage(url: String?, requestOptions: RequestOptions = requestOptionsDefault) {
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(factory))
        .apply(requestOptions)
        .into(this)
}

private val factory by lazy {
    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
}

private val requestOptionsUserImage by lazy {
    RequestOptions().circleCrop().placeholder(R.drawable.ic_image_placeholder)
}

private val requestOptionsDefault by lazy {
    RequestOptions().placeholder(R.drawable.ic_image_placeholder)
}
