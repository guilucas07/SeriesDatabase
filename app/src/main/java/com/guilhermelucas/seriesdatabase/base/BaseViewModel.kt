package com.guilhermelucas.seriesdatabase.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guilhermelucas.seriesdatabase.BuildConfig
import com.guilhermelucas.seriesdatabase.utils.models.RequestError
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    protected fun showLoading() = _isLoading.postValue(true)
    protected fun hideLoading() = _isLoading.postValue(false)

    protected fun handleThrowable(throwable: Throwable): RequestError {
        if (BuildConfig.DEBUG)
            throwable.printStackTrace()
        return when (throwable) {
            is IOException, is UnknownHostException -> RequestError.NetworkError
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = convertErrorBody(throwable.response()?.errorBody())
                RequestError.HttpError(
                    code,
                    errorResponse
                )
            }
            else -> {
                RequestError.GenericError(
                    throwable.message
                        ?: throwable.localizedMessage.orEmpty()
                )
            }
        }
    }

    private fun convertErrorBody(errorBody: ResponseBody?): String? {
//        val type = object : TypeToken<HashMap<String, String>>() {}.type
//        return try {
//            val errorContent: Map<String, String> = Gson().fromJson(errorBody?.string(), type)
//            errorContent.getValue(errorContent.keys.first())
//        } catch (exception: Exception) {
//            null
//        }
        return null
    }
}