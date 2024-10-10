package com.accessment.task.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accessment.task.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import perfetto.protos.UiState
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {


    fun fetchData() {
        viewModelScope.launch {
            try {
                val result = repository.getData()

                Log.e("error" , " "+ result.data?.data)

            } catch (e: Exception) {
                Log.e("error" , "error" , e)
            }
        }
    }
}