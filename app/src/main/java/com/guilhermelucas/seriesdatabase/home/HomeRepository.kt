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

//    fun getMovie(partialName: String): Observable<List<AdapterItem>> {
//        loadedItems.clear()
//        return movieRepository.getMovie(partialName)
//            .map {
//                it.forEach { movie ->
//                    inflateAdapterItem(movie)
//                }
//                loadedItems
//            }
//    }

    suspend fun loadMoreData(requestStrategy: RequestStrategy = RequestStrategy.NEXT_PAGE): List<Show> {
        val request = when (requestStrategy) {
            RequestStrategy.NEXT_PAGE -> actualPage + 1
            else -> 1
        }

        return seriesDataSource.loadAll(request)

    }

//    private fun inflateAdapterItem(movie: com.guilhermelucas.model.Movie) {
//        loadedItems.add(AdapterItem.MovieItem(movie.toMovieVO(imageUrlBuilder)))
//    }
//
//    fun getAdapterItem(position: Int): AdapterItem? {
//        if (position < loadedItems.size)
//            return loadedItems[position]
//        return null
//    }


}