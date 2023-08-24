package com.example.newsapp.utility

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.example.newsapp.R
import com.example.newsapp.ui.MainActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CustomAlertDialog(context: Context) : Dialog(context) {

    private val dialog = Dialog(context)
    fun alertDialog(
        onDialogPositiveAction: (String) -> Unit,
    ) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_layout)
        Objects.requireNonNull(dialog.window)!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(false)
        val nameEdt = dialog.findViewById<TextInputEditText>(R.id.userName)

        dialog.findViewById<Button>(R.id.btnDialogLogin).setOnClickListener {
            if (nameEdt.text!!.length > 4) {
                onDialogPositiveAction(nameEdt.text.toString().trim())
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                context.startActivity(Intent(context, MainActivity::class.java))

            } else {
                nameEdt.error = context.resources.getString(R.string.error_enter_valid_name)
            }
        }
        dialog.show()
    }
}