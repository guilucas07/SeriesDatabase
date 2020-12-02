package com.guilhermelucas.seriesdatabase

import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.data.datasource.TVMazeApi
import com.guilhermelucas.data.utils.RetrofitHelper
import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.seriesdatabase.detail.di.DetailModules
import com.guilhermelucas.seriesdatabase.home.di.HomeModules
import com.guilhermelucas.seriesdatabase.utils.AndroidResourceProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object AppModules {
    private val basicModule = module {
        single { RetrofitHelper.createService<TVMazeApi>(BuildConfig.API_TV_MAZE) }
        single { SeriesDataSource(get()) }
        single { AndroidResourceProvider(androidContext()) as ResourceProvider }
    }

    val allModules by lazy {
        arrayListOf(basicModule, DetailModules.module, HomeModules.module)
    }
}
