package com.example.newsapp.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.utility.isNetworkAvailable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val networkLiveData = MutableSharedFlow<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _networkAlerts = MutableSharedFlow<Boolean>()
    val networkAlerts = _networkAlerts.asSharedFlow()

    var cancelNetworkRequest = false
    private var networkCallJob: Job? = null


    fun setIsLoading(isLoading: Boolean?) {
        this._isLoading.postValue(isLoading)
    }

    fun validateNetwork(onNetworkConnected: suspend () -> Unit) {
        networkCallJob = viewModelScope.launch {
            testNetwork()
            if (isActive) {
                onNetworkConnected()
            }
        }
    }

    private suspend fun testNetwork() {
        val isConnected = getApplication<Application>().isNetworkAvailable().not()
        _networkAlerts.emit(isConnected)
        if (isConnected && cancelNetworkRequest.not()) {
            delay(timeMillis = 2000)
            testNetwork()
        } else if (cancelNetworkRequest) {
            networkCallJob?.cancel()
            cancelNetworkRequest = false
        }
    }
}