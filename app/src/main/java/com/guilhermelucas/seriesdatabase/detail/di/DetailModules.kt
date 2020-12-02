package com.guilhermelucas.seriesdatabase.detail.di

import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.seriesdatabase.BuildConfig
import com.guilhermelucas.seriesdatabase.detail.SeriesDetailViewModel
import com.guilhermelucas.seriesdatabase.detail.data.SeriesDetailRepository
import com.guilhermelucas.seriesdatabase.utils.AndroidResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DetailModules {
    val module = module {
        single { SeriesDetailRepository(get()) }
        viewModel { (seriesId: Int) -> SeriesDetailViewModel(seriesId, get(), get()) }
    }
}
