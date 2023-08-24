package com.example.newsapp.ui.dashboard.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DashboardResponse(
    @SerializedName("results")
    var result: ArrayList<CategoryData>

)
@Parcelize
data class CategoryData(
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("image_url")
    var image_url: String?,
    @SerializedName("pubDate")
    var pubDate: String?,
    @SerializedName("link")
    var link: String?,

): Parcelable

