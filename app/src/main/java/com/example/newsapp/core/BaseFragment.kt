package com.example.newsapp.core

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.AppLoadingDialogBinding

import timber.log.Timber


open class BaseFragment : Fragment() {
    private var progressDialog: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Timber.e("Orientation : " + resources.configuration.orientation)
    }


    open fun showLoading() {
        if (progressDialog == null) {
            progressDialog = Dialog(requireContext(), R.style.CustomDialog)
        } else {
            if (progressDialog?.isShowing == false) {
                progressDialog?.show()
            }
            return
        }

        val progressBinding = AppLoadingDialogBinding.inflate(LayoutInflater.from(requireContext()))
        progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog?.setContentView(progressBinding.root)
        progressDialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(), android.R.color.transparent
            )
        )
        progressDialog?.setCancelable(false)
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.show()
    }

    open fun hideLoading() {
        progressDialog?.dismiss()
    }

}
