package com.accessment.task.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accessment.task.data.database.AppDatabase
import com.accessment.task.data.database.MedResponseDao
import com.accessment.task.data.database.MedicineResponseEntity
import com.accessment.task.data.models.BaseResponseModel
import com.accessment.task.data.models.MedicineResponse
import com.accessment.task.repository.AppRepository
import com.accessment.task.utils.NetworkResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import perfetto.protos.UiState
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: AppRepository ,private val medResponseDao: MedResponseDao , private val gson : Gson) : ViewModel() {


    private val _medicineResponse: MutableLiveData<NetworkResult<BaseResponseModel<List<MedicineResponse>>>> =
        MutableLiveData()
    val medicineResponse: LiveData<NetworkResult<BaseResponseModel<List<MedicineResponse>>>> =
        _medicineResponse


    fun fetchData() {
        _medicineResponse.value = NetworkResult.Loading(true)
        viewModelScope.launch {
            delay(1000)
                val response = repository.getData()
            _medicineResponse.value = NetworkResult.Loading(false)

            response.exception?.let { exception ->
                val data = getMedicineResponseList(medResponseDao)
                if(data != null)
                {
                    _medicineResponse.value = NetworkResult.Success(BaseResponseModel(statusCode = 200 , messageList = "ok" , data = data))
                }
                else
                {
                    _medicineResponse.value = NetworkResult.Error(exception.message ?:"", exception)
                }
                return@launch
            }

            response.data?.let { data ->
                if (data.statusCode == 200 ) {

                    _medicineResponse.value = NetworkResult.Success(data)
                    saveMedicineResponseList(data.data,medResponseDao)

                } else {
                    data.messageList.firstOrNull()?.let {
                        _medicineResponse.value = NetworkResult.Error(it.toString())
                    } ?: run {
                        _medicineResponse.value = NetworkResult.Error("")
                    }
                }
                return@launch
            }

            response.message?.let { message ->
                _medicineResponse.value = NetworkResult.Error(message)
                return@launch
            }

            _medicineResponse.value = NetworkResult.Error("")


        }
    }

    suspend fun saveMedicineResponseList(medicineList: List<MedicineResponse>, dao: MedResponseDao) {
        dao.deleteAllResponses() // Clear old data
        val jsonString = gson.toJson(medicineList)
        val entity = MedicineResponseEntity(responseData = jsonString)
        dao.insertResponse(entity)
    }

    suspend fun getMedicineResponseList(dao: MedResponseDao): List<MedicineResponse>? {
        val jsonResponse = dao.getResponse() ?: return null
        val type = object : TypeToken<List<MedicineResponse>>() {}.type
        return gson.fromJson(jsonResponse, type)
    }
}