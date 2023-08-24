package com.example.newsapp.api


data class ApiResult<T>(
    var status: String? = null,
    var totalResults: String? = null,
    var results: String? = null,
    var errorType: String? = null,
    /*   var resultCode: Int? = null,
       var errorType: String? = null,
       var errorDescription: String? = null,
       var otherData: String? = null,
       var resultData: T? = null,*/
)
