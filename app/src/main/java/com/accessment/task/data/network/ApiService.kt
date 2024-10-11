package com.accessment.task.data.network

import com.accessment.task.data.models.BaseResponseModel
import com.accessment.task.data.models.MedicineResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("74790fde-8fdb-40bf-8ea6-02cb29bd4969")
    suspend fun getData(): Response<BaseResponseModel<List<MedicineResponse>>>
}