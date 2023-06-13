package com.assign.androidinterntask.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.assign.androidinterntask.interfaces.DashboardHttpInterface
import com.assign.androidinterntask.interfaces.DashboardRepository
import com.assign.androidinterntask.model.DashboardDetails
import com.assign.androidinterntask.utils.AppDelegate
import com.assign.androidinterntask.utils.Resource
import com.assign.androidinterntask.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepositoryImpl() : DashboardRepository {

    private val dashboardHttpInterface: DashboardHttpInterface

    init {
        dashboardHttpInterface = RetrofitClient.getClientWithInterceptor(AppDelegate.getInstance().bearerToken).create(DashboardHttpInterface::class.java)
    }

    override fun getNewDashboard(): LiveData<Resource<DashboardDetails>> {
        val result: MutableLiveData<Resource<DashboardDetails>> = MutableLiveData<Resource<DashboardDetails>>()
        dashboardHttpInterface.getNewDashboard()
            .enqueue(object : Callback<DashboardDetails> {
                override fun onResponse(
                    call: Call<DashboardDetails>,
                    response: Response<DashboardDetails>,
                ) {
                    if (response.isSuccessful()) {
                        result.setValue(Resource.Companion.success(response.body()))
                    } else {
                        result.setValue(Resource.Companion.error("Error", null))
                    }
                }

                override fun onFailure(call: Call<DashboardDetails>, t: Throwable) {
                    result.setValue(Resource.Companion.error("Error", null))
                }
            })
        return result
    }
}