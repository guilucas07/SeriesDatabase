package com.guilhermelucas.data.utils

import android.util.Log
import com.guilhermelucas.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitHelper {

    inline fun <reified T> createService(
        baseUrl: String
    ): T =
        createClient(
            baseUrl
        ).create(T::class.java)

    fun createClient(
        baseUrl: String
    ): Retrofit {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor { message -> Log.d("HttpLoggingInterceptor", message) }
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addNetworkInterceptor(httpLoggingInterceptor)
        }

        val client = builder.build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

}
