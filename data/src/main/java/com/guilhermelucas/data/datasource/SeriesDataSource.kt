package com.guilhermelucas.data.datasource

import com.guilhermelucas.data.utils.toShow
import com.guilhermelucas.model.Show

class SeriesDataSource(private val tvMazeApi: TVMazeApi) {
    suspend fun loadAll(page: Int? = null): List<Show> {
        return tvMazeApi.getAllShows(page).map { it.toShow() }
    }
}
