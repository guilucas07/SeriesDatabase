package com.guilhermelucas.seriesdatabase.utils.models

sealed class RequestError {
    data class HttpError(val code: Int, val error: String? = null) : RequestError()
    data class GenericError(val error: String) : RequestError()
    object NetworkError : RequestError()
}