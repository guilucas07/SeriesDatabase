package com.guilhermelucas.seriesdatabase.utils.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guilhermelucas.seriesdatabase.base.BaseViewModelFactory

inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this,
            BaseViewModelFactory(creator)
        ).get(T::class.java)
}

fun <T> Fragment.setupObserver(
    pair: Pair<LiveData<T>, (T) -> Unit>
) = pair.first.observe(viewLifecycleOwner, Observer { it?.let(pair.second) })

fun Fragment.setupSingleEventObserver(
    pair: Pair<LiveData<Unit>, () -> Unit>
) = pair.first.observe(viewLifecycleOwner, Observer { pair.second() })