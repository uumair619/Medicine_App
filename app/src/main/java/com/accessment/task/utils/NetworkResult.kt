package com.accessment.task.utils

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val exception: Exception? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, exception: Exception? = null, data: T? = null) :
        NetworkResult<T>(data, message, exception)

    class Loading<T>(val isLoading: Boolean) : NetworkResult<T>()
}