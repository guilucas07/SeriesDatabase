package com.guilhermelucas.seriesdatabase.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.guilhermelucas.model.Series
import com.guilhermelucas.seriesdatabase.base.BaseViewModel
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import com.guilhermelucas.seriesdatabase.utils.SingleLiveEvent
import com.guilhermelucas.seriesdatabase.utils.extensions.toAdapterItem
import com.guilhermelucas.seriesdatabase.utils.models.RequestError
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : BaseViewModel() {

    enum class AdapterVisibility {
        EMPTY_VIEW, SEARCH_EMPTY_VIEW, DATA_VIEW
    }

    private enum class ActivityMode {
        DEFAULT, SEARCH
    }

    private var activityMode =
        ActivityMode.DEFAULT

    private val _loadedSeries = MutableLiveData<List<AdapterItem>>()
    val loadedSeries: LiveData<List<AdapterItem>>
        get() = _loadedSeries

    private val _goToDetails = SingleLiveEvent<Int>()
    val goToDetails: LiveData<Int>
        get() = _goToDetails

    private val _changeAdapterVisibility = MutableLiveData<AdapterVisibility>()
    val changeAdapterVisibility: LiveData<AdapterVisibility>
        get() = _changeAdapterVisibility

    private val _showRequestError = SingleLiveEvent<RequestError>()
    val showRequestError: LiveData<RequestError>
        get() = _showRequestError

    private val _scrollToPosition = SingleLiveEvent<Int>()
    val scrollToPosition: LiveData<Int>
        get() = _scrollToPosition

    private var repositoryRequestStrategy =
        HomeRepository.RequestStrategy.FIRST_PAGE

    init {
        tryLoadMoreItems()
    }

    fun onAllItemsAreShowed() {
        tryLoadMoreItems()
    }


    fun searchMovie(query: String) {
        if (query.length >= SEARCH_MIN_LETTERS) {
            viewModelScope.launch {
                runCatching {
                    repository.searchSeries(query)
                }.onSuccess { returnedList ->
                    val newList = returnedList.map { it.toAdapterItem() }
                    _loadedSeries.postValue(newList)

                    _changeAdapterVisibility.postValue(
                        if (newList.isNotEmpty())
                            AdapterVisibility.DATA_VIEW
                        else
                            AdapterVisibility.SEARCH_EMPTY_VIEW
                    )
                    _scrollToPosition.postValue(0)
                    activityMode = ActivityMode.SEARCH
                }.onFailure {
                    activityMode = ActivityMode.DEFAULT
                    _showRequestError.postValue(handleThrowable(it))
                }
            }
        }
    }

    fun onItemClick(position: Int) {
        _loadedSeries.value?.getOrNull(position)?.let {
            _goToDetails.postValue(it.id)

        }
    }

    fun onSwipeToRefresh() {
        loadItems(HomeRepository.RequestStrategy.FIRST_PAGE)
    }

    fun onCloseSearchBar(): Boolean {
        loadItems(HomeRepository.RequestStrategy.FIRST_PAGE)
        return true
    }

    private fun tryLoadMoreItems() {
        if (activityMode == ActivityMode.DEFAULT) {
            loadItems(HomeRepository.RequestStrategy.NEXT_PAGE)
        }
    }

    private fun loadItems(repositoryRequestStrategy1: HomeRepository.RequestStrategy) =
        viewModelScope.launch {
            if (isLoading.value != true) {
                showLoading()

                repositoryRequestStrategy = repositoryRequestStrategy1
                runCatching {
                    repository.loadMoreData(repositoryRequestStrategy)
                }.onSuccess { listMovies: List<Series> ->
                    val newList: List<AdapterItem> =
                        if (repositoryRequestStrategy1 == HomeRepository.RequestStrategy.FIRST_PAGE)
                            listMovies.map { it.toAdapterItem() }
                        else
                            _loadedSeries.value.orEmpty() + listMovies.map { it.toAdapterItem() }

                    _changeAdapterVisibility.postValue(
                        if (newList.isNotEmpty())
                            AdapterVisibility.DATA_VIEW
                        else
                            AdapterVisibility.EMPTY_VIEW
                    )

                    _loadedSeries.postValue(newList)

                    activityMode = ActivityMode.DEFAULT
                    hideLoading()
                }.onFailure {
                    activityMode = ActivityMode.DEFAULT
                    hideLoading()
                    _showRequestError.postValue(handleThrowable(it))
                }
            }
        }

    companion object {
        private const val SEARCH_MIN_LETTERS = 3
    }

}
