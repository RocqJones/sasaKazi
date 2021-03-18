package com.intoverflown.sasakazi.network

import com.intoverflown.sasakazi.data.PostModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    // Mobile data
    @GET("/api/courses/")
    fun fetchMobileData(): Call<List<PostModel>>
}