package com.guilhermelucas.seriesdatabase.utils.extensions

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> ComponentActivity.setupObserver(
    pair: Pair<LiveData<T>, (T) -> Unit>
) = pair.first.observe(this, Observer { it?.let(pair.second) })

fun ComponentActivity.setupSingleEventObserver(
    pair: Pair<LiveData<Unit>, () -> Unit>
) = pair.first.observe(this, Observer { pair.second() })
