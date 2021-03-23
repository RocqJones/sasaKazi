package com.intoverflown.sasakazi.ui.blog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BlogViewModel : ViewModel() {

    private val _webViewUrl = MutableLiveData<String>().apply {
        value = "https://sasakazi.com/blog.html"
    }
    val webUrl: LiveData<String> = _webViewUrl
}