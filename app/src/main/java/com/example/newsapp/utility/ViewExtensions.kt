package com.example.newsapp.utility

import android.view.View

fun View.setVisibility(isVisible: Boolean, isToInvisible: Boolean = false) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else if (isToInvisible) {
        this.visibility = View.INVISIBLE
    } else {
        this.visibility = View.GONE
    }
}


private var lastClickMs: Long = 0
private const val TOO_SOON_DURATION_MS: Long = 1000

fun View.setOnSafeClickListener(onClick:(View)->Unit){
    this.setOnClickListener {
        val nowMs = System.currentTimeMillis()
        if (lastClickMs != 0L && nowMs - lastClickMs < TOO_SOON_DURATION_MS) {
            return@setOnClickListener
        }
        lastClickMs = nowMs
        onClick(this)
    }
}