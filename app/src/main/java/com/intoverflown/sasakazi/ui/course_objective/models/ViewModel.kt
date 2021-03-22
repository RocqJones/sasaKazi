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

    fun fetchMobileData() {
        // use coroutines
        viewModelScope.launch {
            val response = repository.fetchMobileData()
            myMobileResponse.value = response
        }
    }
}