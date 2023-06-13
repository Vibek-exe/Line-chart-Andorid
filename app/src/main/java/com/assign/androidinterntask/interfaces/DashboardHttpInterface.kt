package com.assign.androidinterntask.interfaces

import com.assign.androidinterntask.model.DashboardDetails
import retrofit2.Call
import retrofit2.http.GET

interface DashboardHttpInterface {

    @GET("v1/dashboardNew")
    fun getNewDashboard(): Call<DashboardDetails>
}