package com.guilhermelucas.seriesdatabase.home

import com.guilhermelucas.data.datasource.SeriesDataSource
import com.guilhermelucas.model.Show

class HomeRepository(
    private val seriesDataSource: SeriesDataSource
) {
    private var actualPage: Int = 0

    enum class RequestStrategy {
        FIRST_PAGE, NEXT_PAGE
    }

    suspend fun loadMoreData(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): List<Show> {
        when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage++
            else -> actualPage = 1
        }

        return seriesDataSource.loadAll(actualPage)
    }

}
