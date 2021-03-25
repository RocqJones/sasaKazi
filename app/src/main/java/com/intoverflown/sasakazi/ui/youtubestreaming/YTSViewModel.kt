package com.intoverflown.sasakazi.ui.youtubestreaming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class YTSViewModel : ViewModel(){
    private val _webViewUrl = MutableLiveData<String>().apply {
        value = "https://www.youtube.com/channel/UCEEgRFQRaLHSrCfbizVeomg"
    }
    val ytUrl: LiveData<String> = _webViewUrl
}