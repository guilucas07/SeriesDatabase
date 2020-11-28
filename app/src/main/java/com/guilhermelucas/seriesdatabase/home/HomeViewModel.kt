package com.guilhermelucas.seriesdatabase.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.guilhermelucas.model.Show
import com.guilhermelucas.seriesdatabase.base.BaseViewModel
import com.guilhermelucas.seriesdatabase.utils.models.RequestError
import com.guilhermelucas.seriesdatabase.utils.extensions.toAdapterItem
import com.guilhermelucas.seriesdatabase.home.adapter.AdapterItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : BaseViewModel() {

    enum class AdapterVisibility {
        EMPTY_VIEW, SEARCH_EMPTY_VIEW, DATA_VIEW
    }

    private val _loadedSeries = MutableLiveData<List<AdapterItem>>()
    val loadedSeries: LiveData<List<AdapterItem>>
        get() = _loadedSeries

    private val _goToDetails = MutableLiveData<Int>()
    val goToDetails: LiveData<Int>
        get() = _goToDetails

    private val _changeAdapterVisibility = MutableLiveData<AdapterVisibility>()
    val changeAdapterVisibility: LiveData<AdapterVisibility>
        get() = _changeAdapterVisibility

    private val _showRequestError = MutableLiveData<RequestError>()
    val showRequestError: LiveData<RequestError>
        get() = _showRequestError

    private var activityMode =
        ActivityMode.DEFAULT
    private var isLoading = false
    private var repositoryRequestStrategy =
        HomeRepository.RequestStrategy.FIRST_PAGE

    /*****************************/
    /**     Private objects     **/
    /*****************************/
    private object Constants {
        const val SEARCH_MIN_LETTERS = 3
        const val MAX_ITEMS_EACH_ROW_PORTRAIT = 2
        const val MAX_ITEMS_EACH_ROW_LANDSCAPE = 3
    }

    private enum class ActivityMode {
        DEFAULT, SEARCH
    }

    fun onResume() {
        tryLoadMoreItems()
    }

    fun onItemClick(position: Int) {
        _loadedSeries.value?.getOrNull(position)?.let {
            _goToDetails.postValue(it.id)
        }
    }

    fun tryLoadMoreItems() {
        if (activityMode == ActivityMode.DEFAULT) {
            loadItems(HomeRepository.RequestStrategy.NEXT_PAGE)
        }
    }

    fun onSwipeToRefresh() {
        loadItems(HomeRepository.RequestStrategy.FIRST_PAGE)
    }

    fun onCloseSearchBar(): Boolean {
        loadItems(HomeRepository.RequestStrategy.FIRST_PAGE)
        return true
    }

//    fun searchMovie(partialName: String) {
//        if (partialName.length >= Constants.SEARCH_MIN_LETTERS) {
//            val disposable = repository.getMovie(partialName)
//                .observeOn(schedulers.ui())
//                .subscribeOn(schedulers.io())
//                .subscribe({ listMovies ->
//                    view?.onLoadMovies(listMovies)
//                    if (listMovies.isNotEmpty()) {
//                        view?.changeAdapterVisibility(HomeContract.AdapterVisibility.DATA_VIEW)
//                    } else
//                        view?.changeAdapterVisibility(HomeContract.AdapterVisibility.SEARCH_EMPTY_VIEW)
//
//                    activityMode = ActivityMode.SEARCH
//                }, {
//                    activityMode = ActivityMode.DEFAULT
//                    view?.showError(getErrorType(it))
//                })
//
//            compositeDisposable.add(disposable)
//        }
//    }

    /****************************/
    /**     private methods    **/
    /****************************/

    private fun loadItems(repositoryRequestStrategy1: HomeRepository.RequestStrategy) = viewModelScope.launch {
        if (!isLoading) {
            isLoading = true
            showLoading()

            repositoryRequestStrategy = repositoryRequestStrategy1
                runCatching {
                    repository.loadMoreData(repositoryRequestStrategy)
                }.onSuccess { listMovies: List<Show> ->
                    val lis = _loadedSeries.value.orEmpty()
                    _loadedSeries.postValue(lis + listMovies.map { it.toAdapterItem() })

                    if (listMovies.isNotEmpty()) {
                        _changeAdapterVisibility.postValue(AdapterVisibility.DATA_VIEW)
                    } else
                        _changeAdapterVisibility.postValue(AdapterVisibility.EMPTY_VIEW)

                    activityMode =
                        ActivityMode.DEFAULT
                    hideLoading()
                }.onFailure {
                    activityMode =
                        ActivityMode.DEFAULT
                    isLoading = false
                    hideLoading()
                    _showRequestError.postValue(handleThrowable(it))
                }
            }
        }
}
