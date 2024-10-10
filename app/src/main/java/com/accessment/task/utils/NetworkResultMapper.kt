package com.accessment.task.utils

import com.accessment.task.data.models.BaseResponseModel
import com.google.gson.Gson
import retrofit2.Response

abstract class NetworkResultMapper (val gson: Gson)  {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            else if (response.errorBody()!=null){

                val errorBody: BaseResponseModel<*>? =
                    gson.fromJson(response.errorBody()!!.string(), BaseResponseModel::class.java)
                // Explicit casting to handle the mismatch
                @Suppress("UNCHECKED_CAST")
                return NetworkResult.Error("", data = errorBody as? T)
            }

            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString(), e)
        }
    }

    private fun <T> error(errorMessage: String, exception: Exception?=null): NetworkResult<T> =
        NetworkResult.Error("Request failed $errorMessage", exception)


}