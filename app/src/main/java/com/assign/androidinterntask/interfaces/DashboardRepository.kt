package com.assign.androidinterntask.interfaces

import androidx.lifecycle.LiveData
import com.assign.androidinterntask.model.DashboardDetails
import com.assign.androidinterntask.utils.Resource

interface DashboardRepository {

    fun getNewDashboard(): LiveData<Resource<DashboardDetails>>
}