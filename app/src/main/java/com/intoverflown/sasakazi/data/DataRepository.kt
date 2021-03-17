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
    fun fetchMobileData(): MutableLiveData<List<DataModel>?> {
        val data = MutableLiveData<List<DataModel>?>()

        apiInterface?.fetchMobileData()?.enqueue(object : Callback<List<DataModel>> {

            override fun onFailure(call: Call<List<DataModel>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<List<DataModel>>,
                response: Response<List<DataModel>>
            ) {

                val res = response.body()
                if (response.code() == 200 &&  res!=null){
                    data.value = res
                }else{
                    data.value = null
                }
            }
        })
        Log.d("Repository", data.toString())
        return data
    }

    // Fetch Web Data
}