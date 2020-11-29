package com.guilhermelucas.seriesdatabase.seriesdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.guilhermelucas.model.Show
import com.guilhermelucas.seriesdatabase.base.BaseViewModel
import com.guilhermelucas.model.ResourceProvider
import com.guilhermelucas.seriesdatabase.utils.extensions.toDetailsViewObject
import com.guilhermelucas.seriesdatabase.utils.models.RequestError
import kotlinx.coroutines.launch

class SeriesDetailViewModel(
    private val seriesId: Int,
    private val repository: SeriesDetailRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    private lateinit var loadedSeries : Show
    init {
        viewModelScope.launch {
            runCatching {
                repository.getShow(seriesId)
            }.onSuccess {
                loadedSeries = it
                val basicInfo = loadedSeries.toDetailsViewObject(resourceProvider)
                _seriesBasicInfo.postValue(basicInfo)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    private val _seriesBasicInfo = MutableLiveData<SeriesDetailViewObject>()
    val seriesBasicInfo: LiveData<SeriesDetailViewObject>
        get() = _seriesBasicInfo

    private val _showRequestError = MutableLiveData<RequestError>()
    val showRequestError: LiveData<RequestError>
        get() = _showRequestError

}
