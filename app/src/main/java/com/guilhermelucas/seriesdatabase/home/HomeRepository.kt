package com.guilhermelucas.seriesdatabase.home

import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.model.SearchSeries
import com.guilhermelucas.model.Series

class HomeRepository(
    private val seriesDataSource: SeriesDataSource
) {
    private var actualPage: Int = -1

    enum class RequestStrategy {
        FIRST_PAGE, NEXT_PAGE
    }

    suspend fun loadMoreData(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): List<Series> {
        when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage++
            else -> actualPage = 0
        }

        return seriesDataSource.loadAll(actualPage)
    }

    suspend fun searchSeries(query : String) : List<SearchSeries>{
        return seriesDataSource.searchSeries(query)
    }

}
