package com.accessment.task.data.models


import com.google.gson.annotations.SerializedName

data class BaseResponseModel<T>(
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("returnMessage")
    val messageList: String,
    @SerializedName("data")
    val data: T
)