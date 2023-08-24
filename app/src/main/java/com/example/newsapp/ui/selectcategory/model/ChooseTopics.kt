package com.example.newsapp.selectnews.model

import android.graphics.drawable.Drawable

data class ChooseTopics(
    var topics:String?=null,
    var imgTopics:Drawable?=null,
    var isSelected:Boolean=false
)
