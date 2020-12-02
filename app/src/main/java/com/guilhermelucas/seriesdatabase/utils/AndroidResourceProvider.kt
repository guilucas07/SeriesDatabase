package com.guilhermelucas.seriesdatabase.utils

import android.content.Context
import androidx.annotation.StringRes
import com.guilhermelucas.model.ResourceProvider

class AndroidResourceProvider(private val context: Context) :
    ResourceProvider {
    override fun getString(@StringRes id: Int, vararg params: Any): String {
        return if (params.isNotEmpty())
            context.getString(id, *params)
        else
            context.getString(id)
    }
}
