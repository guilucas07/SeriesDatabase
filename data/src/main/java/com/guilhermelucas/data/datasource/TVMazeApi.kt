package com.guilhermelucas.data.datasource

import com.guilhermelucas.data.model.SearchShowResponse
import com.guilhermelucas.data.model.ShowResponse
import com.guilhermelucas.data.model.ShowWithSeasons
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVMazeApi {

    @GET("shows")
    suspend fun getAllShows(@Query("page") page: Int? = null): List<ShowResponse>

    @GET("search/shows")
    suspend fun searchShow(@Query("q") query: String): List<SearchShowResponse>

    @GET("shows/{id}?embed=seasons")
    suspend fun searchShow(@Path("id") id: Int): ShowWithSeasons

}
