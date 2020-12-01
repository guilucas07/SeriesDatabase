package com.guilhermelucas.seriesdatabase.detail.data

import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.model.Episode
import com.guilhermelucas.model.Series

class SeriesDetailRepository(
    private val seriesDataSource: SeriesDataSource
) {

    suspend fun getSeries(id: Int): Series {
        return seriesDataSource.searchSeries(id)
    }

    suspend fun getSeasonsEpisodes(seasonId : Int) : List<Episode>{
        return seriesDataSource.getSeasonsEpisodes(seasonId)
    }
}
