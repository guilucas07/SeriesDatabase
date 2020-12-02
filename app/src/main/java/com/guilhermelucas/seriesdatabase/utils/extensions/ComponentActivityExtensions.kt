package com.guilhermelucas.seriesdatabase.utils.extensions

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guilhermelucas.seriesdatabase.base.BaseViewModelFactory

inline fun <reified T : ViewModel> ComponentActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(
            this,
            BaseViewModelFactory(creator)
        ).get(T::class.java)
}

fun <T> ComponentActivity.setupObserver(
    pair: Pair<LiveData<T>, (T) -> Unit>
) = pair.first.observe(this, Observer { it?.let(pair.second) })

fun ComponentActivity.setupSingleEventObserver(
    pair: Pair<LiveData<Unit>, () -> Unit>
) = pair.first.observe(this, Observer { pair.second() })
