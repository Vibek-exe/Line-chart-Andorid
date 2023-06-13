package com.assign.androidinterntask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.assign.androidinterntask.interfaces.DashboardRepository
import com.assign.androidinterntask.model.DashboardDetails
import com.assign.androidinterntask.repository.DashboardRepositoryImpl
import com.assign.androidinterntask.utils.Resource

class DashboardViewModel() : ViewModel() {

    private val dashboardRepository: DashboardRepository = DashboardRepositoryImpl()

    fun getNewDashboard() : LiveData<Resource<DashboardDetails>> {
        return dashboardRepository.getNewDashboard()
    }

}