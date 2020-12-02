package com.guilhermelucas.seriesdatabase.base

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guilhermelucas.seriesdatabase.R
import com.guilhermelucas.seriesdatabase.utils.models.RequestError

abstract class BaseActivity : AppCompatActivity() {

    fun showRequestErrorMessage(requestError: RequestError) {
        val message = when (requestError) {
            is RequestError.NetworkError -> R.string.request_error_network
            else -> {
                R.string.request_error_unknown
            }
        }
        Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
    }
}
