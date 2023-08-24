package com.example.newsapp.utility

import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.newsapp.R
import com.example.newsapp.databinding.AlertAdvanceDilogBinding

import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.math.roundToInt

fun Context.getWindowDimension(): Pair<Int, Int> {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return Pair(size.x, size.y)
}


fun Context.isNetworkAvailable(): Boolean {
    var result = false
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    cm?.run {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }
    return result
}

fun Context.getDrawableCompat(resourceId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resourceId)
}

fun Context.getAppVersionName(): String {
    try {
        val pInfo = packageManager.getPackageInfo(packageName, 0)
        return getString(R.string.version, pInfo.versionName.toString())

    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return "N/A"
}

fun Context.getAppVersion(): String {
    try {
        val pInfo = packageManager.getPackageInfo(packageName, 0);
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode.toString()
        } else {
            pInfo.versionCode.toString()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return "0"
}

fun Context.font(font: Int): Typeface? {
    return ResourcesCompat.getFont(this, font)
}

fun Context.loadJSONFromAsset(fileName: String): String? {
    return try {
        val inputStream: InputStream = assets.open(fileName)
        val size: Int = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer)
    } catch (ex: IOException) {
        null
    }
}

fun Context.centerToast(msg: String) {
    val centerToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    centerToast.setGravity(Gravity.CENTER, 0, 0)
    centerToast.show()
}

fun Context.toast(msg: String) {
    val centerToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    centerToast.show()
}


fun Context.d2p(dp: Float): Float {
    return (dp * resources.displayMetrics.density).roundToInt().toFloat()
}

fun Context.showSuccessAlert(
    showTwo: Boolean = false,
    title: String = this.getString(R.string.success),
    message: String,
    okBtnTxt: String = if (showTwo) {
        this.getString(R.string.yes)
    } else {
        this.getString(R.string.ok)
    },
    noBtnText: String = this.getString(R.string.no),
    yesCallback: () -> Unit = {},
    noCallback: () -> Unit = {}
) {
    alertHandling(true, showTwo, title, message, okBtnTxt, noBtnText, {
        yesCallback()
    }, {
        noCallback()
    })
}

fun Context.showFailureAlert(
    showTwoBtns: Boolean = false,
    title: String = this.getString(R.string.alert),
    message: String,
    okBtnTxt: String = if (showTwoBtns) {
        this.getString(R.string.yes)
    } else {
        this.getString(R.string.ok)
    },
    noBtnText: String = this.getString(R.string.no),
    yesCallback: () -> Unit = {},
    noCallback: () -> Unit = {}
) {
    alertHandling(false, showTwoBtns, title, message, okBtnTxt, noBtnText, {
        yesCallback()
    }, {
        noCallback()
    })
}

private fun Context.alertHandling(
    isAlert: Boolean,
    showTwo: Boolean = true,
    title: String,
    message: String,
    okBtnTxt: String = "",
    retryBtnTxt: String = "",
    yesCallback: () -> Unit = {},
    noCallback: () -> Unit = {}
) {
    val alertDialog = AlertDialog.Builder(this).create()
    val binding = AlertAdvanceDilogBinding.inflate(LayoutInflater.from(this))

    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    alertDialog.setCanceledOnTouchOutside(false)
    alertDialog.window?.attributes?.windowAnimations = R.style.popup_window_animation


    binding.tvAltDlgOk.text = if (okBtnTxt.isBlank()) this.getString(R.string.yes) else okBtnTxt
    binding.tvAlertRetry.text =
        if (retryBtnTxt.isBlank()) this.getString(R.string.no) else retryBtnTxt
    binding.tvAltDlgOk.setOnClickListener {
        alertDialog.dismiss()
        yesCallback()
    }
    if (showTwo) {
        binding.tvAlertRetry.setVisibility(true)
        binding.tvAlertRetry.setOnClickListener {
            alertDialog.dismiss()
            noCallback()
        }
    } else binding.tvAlertRetry.setVisibility(false)


    binding.tvAltDlgTitle.text = title
    if (isAlert) {
        binding.tvAltDlgTitle.setTextColor(ContextCompat.getColor(this, R.color.color_primary))
    } else {
        binding.tvAltDlgTitle.setTextColor(ContextCompat.getColor(this, R.color.red))
    }


    binding.tvAltDlgMsg.text = message
    binding.tvAltDlgMsg.setTextColor(ContextCompat.getColor(this, R.color.black))
    alertDialog.setView(binding.root)
    alertDialog.setCancelable(false)
    alertDialog.show()
}


fun Context.datePicker(
    hideDates: Boolean = false,
    isPast: Boolean = false,
    disableDatesFrom: Date = Date(),
    defaultDisplay: Boolean = false,
    defaultValue: String = "",
    changeToReqFormat: Boolean = false,
    inputFormat: String = "MM-dd-yyyy",
    outputFormat: String = "MM-dd-yyyy",
    result: (String) -> Unit
) {

    val orgOutputFormat = "MM-dd-yyyy"
    val orgInputFormat = "MM-dd-yyyy"
    val c = Calendar.getInstance()
    c.timeZone = TimeZone.getDefault() //TimeZone.getTimeZone("Asia/Kolkata")
    val mYear = c.get(Calendar.YEAR)
    val mMonth = c.get(Calendar.MONTH)
    val mDay = c.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
        val date = addZeroToMonth(monthOfYear + 1) + "-" + addZeroToDate(dayOfMonth) + "-" + year
        if (changeToReqFormat) {
            val resultDate = DateConverter.changeDateFormat(date, orgInputFormat, outputFormat)
            result(resultDate)
        } else {
            result(date)
        }
    }, mYear, mMonth, mDay)

    if (hideDates) {
        if (isPast) {
            datePickerDialog.datePicker.maxDate = disableDatesFrom.time
        } else {
            datePickerDialog.datePicker.minDate = disableDatesFrom.time
        }
    }

    if (defaultDisplay) {
        val modifiedDate = defaultValue.trim().replace("/", "-")
        if (modifiedDate.isNotBlank()) {
            if (DateConverter.changeDateFormat(modifiedDate, inputFormat, orgOutputFormat)
                    .isNotEmpty()
            ) {
                val dateList = DateConverter.changeDateFormat(
                    modifiedDate,
                    inputFormat,
                    orgOutputFormat
                ).split("-")
                if (dateList.isNotEmpty() && dateList.size == 3) {
                    datePickerDialog.datePicker.updateDate(
                        dateList[2].toInt(),
                        (dateList[0].toInt()).minus(1),
                        dateList[1].toInt()
                    )
                }
            }
        }
    }
    datePickerDialog.show()
}

private fun addZeroToMonth(dayOfMonth: Int): String {
    return if (dayOfMonth < 10) {
        "0$dayOfMonth"
    } else dayOfMonth.toString()
}

private fun addZeroToDate(dayOfDate: Int): String {
    return if (dayOfDate < 10) {
        "0$dayOfDate"
    } else dayOfDate.toString()
}

fun changeColorOfFilterButtons(
    cx: Context,
    state: Boolean,
    btnContinue: AppCompatButton
) {
    if (state) {
        btnContinue.setBackgroundResource(R.drawable.bg_button_selected)
        // btnContinue.setTextColor(cx.resources.getColor(R.color.white))
    } else {
        btnContinue.setBackgroundResource(R.drawable.bg_button_unselected)
    }
    btnContinue.isEnabled = state
}

fun searchRestriction(newText: String, bindingVariable: SearchView) {
    if (newText!!.isNotEmpty()) {
        if (!newText?.last()!!.isLetterOrDigit() && !newText?.last()!!.isWhitespace()) {
            var l = (newText?.length)
            if (l != null) l -= 1
            bindingVariable.setQuery(newText.substring(0, l!!)!!, false)
        }

    }
}

