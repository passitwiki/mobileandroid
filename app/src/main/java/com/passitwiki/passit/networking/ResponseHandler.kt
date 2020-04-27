package com.passitwiki.passit.networking

import android.util.Log
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Class for handling responses - successful or ones where there was an exception.
 * Its functions return a Resource object with successful data obtained or a message
 * when there was an exception.
 */
open class ResponseHandler {
    fun <T : Any> handleSuccess(data: T?): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(exception: Exception): Resource<T> {
        Log.d("MyTagExplitcit", exception.message + "\n" + exception.stackTrace)
        return when (exception) {
            is SocketTimeoutException -> Resource
                .error("Please refresh, there was a timeout.", null)
            is UnknownHostException -> Resource
                .error("Please check Your internet connection.", null)
            is HttpException -> Resource.error(getErrorMessage(exception.code()), null)
            else -> Resource.error("Something went wrong. Please refresh.", null)
        }
    }

    private fun getErrorMessage(httpCode: Int): String {
        return when (httpCode) {
            201 -> "Created. To see, please refresh."
            204 -> "Success. Please refresh."
            400 -> "Invalid password. Try again."
            401 -> "Unauthorised. Please refresh."
            404 -> "Not found. Please refresh."
            else -> httpCode.toString()
        }
    }
}