package com.accessment.task.data.network

import com.accessment.task.data.models.BaseResponseModel
import com.accessment.task.data.models.MedicineResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("73afc239-e1cc-4d14-b7b0-5c9460b29e73")
    suspend fun getData(): Response<BaseResponseModel<List<MedicineResponse>>>
}