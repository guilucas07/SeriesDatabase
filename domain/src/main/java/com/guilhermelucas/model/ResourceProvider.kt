package com.guilhermelucas.model

interface ResourceProvider {
    fun getString(id: Int, vararg params: Any): String
}
