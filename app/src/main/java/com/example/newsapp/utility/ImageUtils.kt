package com.example.newsapp.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.newsapp.R

fun convertStringToBitmap(str: String?, context: Context,gender:String=""): Bitmap {
    return if (str.isNullOrBlank().not()) {
        val imageBytes = Base64.decode(str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }else if (gender.startsWith("f",true)) {
        BitmapFactory.decodeResource(context.resources, R.drawable.virtual_women)
    }else if (gender.startsWith("m",true)) {
        BitmapFactory.decodeResource(context.resources, R.drawable.virtual_men)
    }else{
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_baseline_person_24)
    }
}