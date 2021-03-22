package com.intoverflown.sasakazi.data

import com.intoverflown.sasakazi.network.RetrofitInstance
import retrofit2.Response

/**  fetch all the data **/
class Repository {
    // Fetch Mobile Data
    suspend fun fetchMobileData() : Response<PostModel> {
        return RetrofitInstance.api.fetchMobileData()
    }

    // Fetch Web Data
}