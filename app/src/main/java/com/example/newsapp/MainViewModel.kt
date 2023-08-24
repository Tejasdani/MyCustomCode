package com.example.newsapp

import android.app.Application
import com.example.newsapp.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val app: Application) : BaseViewModel(app)  {

}