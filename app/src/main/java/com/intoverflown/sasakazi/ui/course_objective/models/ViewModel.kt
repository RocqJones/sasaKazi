package com.intoverflown.sasakazi.ui.course_objective.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intoverflown.sasakazi.data.PostModel
import com.intoverflown.sasakazi.data.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewModel(private val repository: Repository) : ViewModel() {

    val myMobileResponse: MutableLiveData<Response<PostModel>> = MutableLiveData()

    // Mobile
    fun fetchMobileData() {
        // use coroutines
        viewModelScope.launch {
            val response = repository.fetchMobileData()
            myMobileResponse.value = response
        }
    }

    // Web
    fun fetchWebData() {
        // use coroutines
        viewModelScope.launch {
            val response = repository.fetchWebData()
            myMobileResponse.value = response
        }
    }

    // Game
    fun fetchGameData() {
        // use coroutines
        viewModelScope.launch {
            val response = repository.fetchGameData()
            myMobileResponse.value = response
        }
    }

    // Online Safety
    fun fetchOnlineSafetyData() {
        // use coroutines
        viewModelScope.launch {
            val response = repository.fetchOnlineSafetyData()
            myMobileResponse.value = response
        }
    }
}