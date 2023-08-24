package com.example.newsapp.core

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.newsapp.R
import timber.log.Timber


abstract class BaseVMBindingFragment<T : ViewBinding, VM : BaseViewModel>(private var viewModelClass: Class<VM>) : BaseBindingFragment<T>() {

    lateinit var viewModel: VM

    private lateinit var noNetworkDialog: AlertDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[viewModelClass]


        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoading()
            } else {
                hideLoading()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.networkLiveData.collect {
                onNetworkChange(it)
            }
        }



        noNetworkDialog = networkAlertDialog()
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

    /**
     * Here network change callback occur
     * @param isConnected whether device is connected with network or not
     */
    open fun onNetworkChange(isConnected: Boolean) {
        Timber.tag("Fragment Connected:").e(if (isConnected) "On Connect" else "On Disconnect")
    }

    private fun networkAlertDialog(): AlertDialog {
        val alert = AlertDialog.Builder(requireContext()).apply {
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