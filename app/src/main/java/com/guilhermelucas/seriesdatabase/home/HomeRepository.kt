package com.guilhermelucas.seriesdatabase.home

import com.guilhermelucas.data.datasource.ShowDataSource
import com.guilhermelucas.model.SearchShow
import com.guilhermelucas.model.Show

class HomeRepository(
    private val showDataSource: ShowDataSource
) {
    private var actualPage: Int = -1

    enum class RequestStrategy {
        FIRST_PAGE, NEXT_PAGE
    }

    suspend fun loadMoreData(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): List<Show> {
        when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage++
            else -> actualPage = 0
        }

        return showDataSource.loadAll(actualPage)
    }

    suspend fun searchShow(query : String) : List<SearchShow>{
        return showDataSource.searchShow(query)
    }

}
