package com.accessment.task.repository

import com.accessment.task.data.network.ApiService
import com.accessment.task.utils.NetworkResultMapper
import com.google.gson.Gson
import javax.inject.Inject

class AppRepository @Inject constructor(private val apiService: ApiService ,gson: Gson) :NetworkResultMapper(gson){

        suspend fun getData() = safeApiCall {
                apiService.getData()
        }
}
