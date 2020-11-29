package com.guilhermelucas.data.datasource

import com.guilhermelucas.data.utils.toSearchShow
import com.guilhermelucas.data.utils.toShow
import com.guilhermelucas.model.SearchShow
import com.guilhermelucas.model.Show

class ShowDataSource(private val tvMazeApi: TVMazeApi) {
    suspend fun loadAll(page: Int? = null): List<Show> {
        return tvMazeApi.getAllShows(page).map { it.toShow() }
    }

    suspend fun searchShow(query: String): List<SearchShow> {
        return tvMazeApi.searchShow(query).map { it.toSearchShow() }
    }

    suspend fun searchShow(id: Int): Show {
        return tvMazeApi.searchShow(id).toShow()
    }
}
