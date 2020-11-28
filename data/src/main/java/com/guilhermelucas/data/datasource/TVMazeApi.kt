package com.guilhermelucas.data.datasource

import com.guilhermelucas.data.model.ShowResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TVMazeApi {

    @GET("shows")
    suspend fun getAllShows(@Query("page") page: Int? = null) : List<ShowResponse>

}
