package com.intoverflown.sasakazi.network

import com.intoverflown.sasakazi.data.PostModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    // Mobile data
    @GET("api/courses/2")
    suspend fun fetchMobileData(): Response<PostModel>
}