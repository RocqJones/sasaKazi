package com.intoverflown.sasakazi.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.intoverflown.sasakazi.network.ApiClient
import com.intoverflown.sasakazi.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository {
    /**  fetch all the data **/
    private var apiInterface: ApiInterface? = null

    // initialize API client
    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    }

    // Fetch Mobile Data
    fun fetchMobileData(): LiveData<List<PostModel>> {
        val data = MutableLiveData<List<PostModel>>()

        apiInterface?.fetchMobileData()?.enqueue(object : Callback<List<PostModel>> {
            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                null.also { data.value = it }
                Log.e("Data Repository", "Problem calling API")
            }

            override fun onResponse(
                call: Call<List<PostModel>>,
                response: Response<List<PostModel>>
            ) {

                val res = response.body()
                if (response.code() == 200 && res != null){
                    res.also { data.value = it }
                    // print data
                    Log.d("Results: ", res.toString())
                }else{
                    null.also { data.value = it }
                }
            }
        })
        Log.d("Repository Data", data.toString())
        return data
    }

    // Fetch Web Data
}