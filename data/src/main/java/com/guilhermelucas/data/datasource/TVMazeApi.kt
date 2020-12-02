package com.guilhermelucas.data.datasource

import com.guilhermelucas.data.model.EpisodeResponse
import com.guilhermelucas.data.model.SearchSeriesResponse
import com.guilhermelucas.data.model.SeriesResponse
import com.guilhermelucas.data.model.SeriesWithSeasons
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVMazeApi {

    @GET("shows")
    suspend fun getAllSeries(@Query("page") page: Int? = null): List<SeriesResponse>

    @GET("search/shows")
    suspend fun searchSeries(@Query("q") query: String): List<SearchSeriesResponse>

    @GET("shows/{id}?embed=seasons")
    suspend fun searchSeries(@Path("id") id: Int): SeriesWithSeasons

    @GET("seasons/{id}/episodes")
    suspend fun seasonsEpisodes(@Path("id") id: Int): List<EpisodeResponse>

}
