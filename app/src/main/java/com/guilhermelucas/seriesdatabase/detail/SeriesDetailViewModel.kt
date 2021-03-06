package com.guilhermelucas.seriesdatabase.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.model.Series
import com.guilhermelucas.seriesdatabase.base.BaseViewModel
import com.guilhermelucas.seriesdatabase.detail.ui.adapter.model.EpisodeViewObject
import com.guilhermelucas.seriesdatabase.detail.data.SeriesDetailRepository
import com.guilhermelucas.seriesdatabase.detail.model.SeriesDetailViewObject
import com.guilhermelucas.seriesdatabase.utils.extensions.toDetailsViewObject
import com.guilhermelucas.seriesdatabase.utils.extensions.toEpisodeViewObject
import com.guilhermelucas.seriesdatabase.utils.models.RequestError
import kotlinx.coroutines.launch

class SeriesDetailViewModel(
    private val seriesId: Int,
    private val repository: SeriesDetailRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private var loadedSeries: Series? = null

    init {
        viewModelScope.launch {
            runCatching {
                repository.getSeries(seriesId)
            }.onSuccess {
                loadedSeries = it
                val basicInfo = loadedSeries?.toDetailsViewObject(resourceProvider)
                _seriesBasicInfo.postValue(basicInfo)

                if (loadedSeries?.seasons?.size != 0)
                    onSpinnerSeasonUpdated(0)

            }.onFailure {
                _showRequestError.postValue(handleThrowable(it))
            }
        }
    }

    private val _seriesBasicInfo = MutableLiveData<SeriesDetailViewObject>()
    val seriesBasicInfo: LiveData<SeriesDetailViewObject>
        get() = _seriesBasicInfo

    private val _showRequestError = MutableLiveData<RequestError>()
    val showRequestError: LiveData<RequestError>
        get() = _showRequestError

    private val _seasonEpisodes = MutableLiveData<List<EpisodeViewObject>>()
    val seasonEpisodes: LiveData<List<EpisodeViewObject>>
        get() = _seasonEpisodes

    private val _isLoadingSeasonEpisodes = MutableLiveData<Boolean>()
    val isLoadingSeasonEpisodes: LiveData<Boolean>
        get() = _isLoadingSeasonEpisodes

    fun onSpinnerSeasonUpdated(position: Int) = viewModelScope.launch {
        _isLoadingSeasonEpisodes.postValue(true)
        loadedSeries?.seasons?.getOrNull(position)?.let {
            runCatching {
                repository.getSeasonsEpisodes(it.id)
            }.onSuccess { episodes ->
                _seasonEpisodes.postValue(episodes.map { it.toEpisodeViewObject(resourceProvider) })
                _isLoadingSeasonEpisodes.postValue(false)
            }.onFailure {
                it.printStackTrace()
                _isLoadingSeasonEpisodes.postValue(false)
            }
        }
    }

}
