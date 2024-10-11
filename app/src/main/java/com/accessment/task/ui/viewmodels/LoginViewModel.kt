package com.accessment.task.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accessment.task.data.models.BaseResponseModel
import com.accessment.task.repository.AppRepository
import com.accessment.task.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: AppRepository)  : ViewModel(){

    private val _loginResponse: MutableLiveData<NetworkResult<BaseResponseModel<String>>> =
        MutableLiveData()
    val loginResponse: LiveData<NetworkResult<BaseResponseModel<String>>> =
        _loginResponse

    fun login(usernameOrEmail: String, password: String) {

        _loginResponse.value = NetworkResult.Loading(true)
        viewModelScope.launch {
            delay(2000)
            _loginResponse.value = NetworkResult.Success(BaseResponseModel(statusCode = 200 , messageList = "ok" , data = usernameOrEmail))
        }





    }
    /*
    private val _loginResponse: MutableStateFlow<NetworkResult<BaseResponseModel<String>>>()
   val loginResponse: StateFlow<NetworkResult<BaseResponseModel<String>>> get() =
       _loginResponse

    */
}