package com.guilhermelucas.seriesdatabase.home.di

import com.guilhermelucas.seriesdatabase.home.HomeRepository
import com.guilhermelucas.seriesdatabase.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModules {
    val module = module {
        single { HomeRepository(get()) }
        viewModel { HomeViewModel(get(), get()) }
    }
}
