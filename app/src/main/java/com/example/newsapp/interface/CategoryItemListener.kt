package com.example.newsapp.`interface`

import android.view.View
import com.example.newsapp.ui.dashboard.model.CategoryData

interface RVListner  {
    fun onClickedItem(view: View,list: List<CategoryData>)
}