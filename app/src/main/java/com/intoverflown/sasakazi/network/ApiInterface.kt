package com.intoverflown.sasakazi.network

import com.intoverflown.sasakazi.data.DataModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    // Mobile data
    @GET("2")
    fun fetchMobileData(): Call<List<DataModel>>
}