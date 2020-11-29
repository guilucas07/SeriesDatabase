package com.guilhermelucas.seriesdatabase.seriesdetail

import com.guilhermelucas.data.datasource.ShowDataSource
import com.guilhermelucas.model.Episode
import com.guilhermelucas.model.Show

class SeriesDetailRepository(
    private val showDataSource: ShowDataSource
) {

    suspend fun getShow(id: Int): Show {
        return showDataSource.searchShow(id)
    }

    suspend fun getSeasonsEpisodes(seasonId : Int) : List<Episode>{
        return showDataSource.getSeasonsEpisodes(seasonId)
    }
}
