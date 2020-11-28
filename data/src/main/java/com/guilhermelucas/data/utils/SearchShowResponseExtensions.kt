package com.guilhermelucas.data.utils

import com.guilhermelucas.data.model.SearchShowResponse
import com.guilhermelucas.model.SearchShow

fun SearchShowResponse.toSearchShow() =
    SearchShow(score, show.toShow())
