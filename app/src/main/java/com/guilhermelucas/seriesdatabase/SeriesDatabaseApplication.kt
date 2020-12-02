package com.guilhermelucas.seriesdatabase

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SeriesDatabaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SeriesDatabaseApplication)
            modules(AppModules.allModules)
        }
    }
}