package com.example.newsapp.ui.dashboard

import com.example.newsapp.api.ApiService
import com.example.newsapp.api.Resource
import com.example.newsapp.api.safeApiCall
import com.example.newsapp.ui.dashboard.model.DashboardResponse
import javax.inject.Inject

/** Created by Tejas Dani on 18-Feb-2023
 * for call api service of dashboard screen
 * */
class HomeRepo  @Inject constructor(val apiService: ApiService) {
    suspend fun callDashboardApi(category: String): Resource<DashboardResponse?> {
        return safeApiCall {
            apiService.dashboardExtends(category)
        }
    }
    suspend fun searchNewsApi(category: String): Resource<DashboardResponse?> {
        return safeApiCall {
            apiService.searchExtends(category)
        }
    }

}