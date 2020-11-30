package com.guilhermelucas.data.datasource

import com.guilhermelucas.data.utils.toEpisode
import com.guilhermelucas.data.utils.toSearchSeries
import com.guilhermelucas.data.utils.toSeries
import com.guilhermelucas.model.Episode
import com.guilhermelucas.model.SearchSeries
import com.guilhermelucas.model.Series

class SeriesDataSource(private val tvMazeApi: TVMazeApi) {
    suspend fun loadAll(page: Int? = null): List<Series> {
        return tvMazeApi.getAllSeries(page).map { it.toSeries() }
    }

    suspend fun searchSeries(query: String): List<SearchSeries> {
        return tvMazeApi.searchSeries(query).map { it.toSearchSeries() }
    }

    suspend fun searchSeries(id: Int): Series {
        return tvMazeApi.searchSeries(id).toSeries()
    }

    suspend fun getSeasonsEpisodes(id: Int): List<Episode> {
        return tvMazeApi.seasonsEpisodes(id).map {
            it.toEpisode()
        }
    }
}


