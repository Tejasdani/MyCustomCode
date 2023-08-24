package com.example.newsapp.core

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.newsapp.R
import timber.log.Timber


abstract class BaseVMBindingActivity<T : ViewBinding, VM : BaseViewModel>(private var viewModelClass: Class<VM>) :
    BaseBindingActivity<T>() {

    lateinit var viewModel: VM

    lateinit var noNetworkDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[viewModelClass]
        noNetworkDialog = networkAlertDialog()
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.networkLiveData.collect {
                onNetworkChange(it)
            }
        }



        lifecycleScope.launchWhenStarted {
            viewModel.networkAlerts.collect { show->
                if (show) {
                    if (!noNetworkDialog.isShowing && viewModel.cancelNetworkRequest.not()) {
                        noNetworkDialog.show()
                    }
                } else {
                    if (noNetworkDialog.isShowing) {
                        noNetworkDialog.dismiss()
                    }
                }
            }
        }

    }


    open fun onNetworkChange(isConnected: Boolean) {
        Timber.tag("Activity Connected:").e(if (isConnected) "On Connect" else "On Disconnect")
    }

    private fun networkAlertDialog(): AlertDialog {
        val alert = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.network_disconnected))
            setMessage(getString(R.string.please_check_the_network_and_retry))
            setPositiveButton(getString(R.string.retry)) { dialog, i ->

            }
            setNegativeButton(getString(R.string.cancel)) { dialog, i ->
                viewModel.cancelNetworkRequest = true
            }
        }
        return alert.create()
    }

}