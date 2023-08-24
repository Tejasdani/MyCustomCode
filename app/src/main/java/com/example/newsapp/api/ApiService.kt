package com.example.newsapp.api

import com.example.newsapp.constants.Constants
import com.example.newsapp.constants.Constants.CATEGORY
import com.example.newsapp.constants.Constants.SEARCH_PARAM
import com.example.newsapp.ui.dashboard.model.DashboardResponse
import com.example.newsapp.ui.dashboard.model.PersonalDetailsResponse
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET(Constants.HOME_PARAM)
    suspend fun dashboardExtends(@Query(CATEGORY) category: String): Response<DashboardResponse>

    @GET(Constants.HOME_PARAM)
    suspend fun searchExtends(@Query(SEARCH_PARAM) category: String): Response<DashboardResponse>

    @GET("personalDetails.json")
    suspend fun getPersonalDetails(): Response<PersonalDetailsResponse>
}

interface DashboardService : ApiService {
   /* @GET(Constants.HOME_PARAM)
    suspend fun dashboardExtends(@Query(CATEGORY) category: String): Response<DashboardResponse>
*/

}