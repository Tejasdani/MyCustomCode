package com.example.newsapp.utility

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateConverter {

    fun convertStringToDate(passedDate: String, inputFormat: String): Date? {
        var date: Date?=null
        try {
            val am_pm = ""
            val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            date = simpleDateFormat.parse(passedDate)
        } catch (e: Exception) {
            return date
        }

        return date
    }

    fun changeDateFormat(passedDate: String, inputFormat: String, outputFormat: String): String {
        return try {
            val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
            val date = simpleDateFormat.parse(passedDate)
            val simpleDateFormat1 = SimpleDateFormat(outputFormat)
            simpleDateFormat1.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun changeDateFormat(passedDate: String, inputFormat: String, outputFormat: String, ampm: String): String? {
        var date: Date? = null
        var format = ""
        try {
            var am_pm = ""
            val simpleDateFormat = SimpleDateFormat(inputFormat)
            date = simpleDateFormat.parse(passedDate)
            val simpleDateFormat1 = SimpleDateFormat(outputFormat)
            format = simpleDateFormat1.format(date)
            if (ampm.equals("yes", ignoreCase = true)) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                    am_pm = "AM"
                } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                    am_pm = "PM"
                }
                format = "$format $am_pm"
            }
        } catch (e: Exception) {
            return null
        }

        return format
    }

    fun convertStringToTime(passedTIme: String, inputFormat: String, outputFormat: String): String? {
        val df = SimpleDateFormat(inputFormat)
        val outperform = SimpleDateFormat(outputFormat)
        var date: Date? = null
        var output: String? = null
        return try {
            date = df.parse(passedTIme)
            outperform.format(date)
        } catch (pe: Exception) {
            pe.printStackTrace()
            ""
        }

    }

    @JvmStatic
    fun getCurrentDateTimeWithPair(dateFormat: String = "MM-dd-yyyy", timeFormat: String = "hh:mm:ss aa"): Pair<String, String> {
        val formatter = SimpleDateFormat(dateFormat)
        val currentDate = Date()
        val timeFormatter = SimpleDateFormat(timeFormat)
        return Pair(formatter.format(currentDate), timeFormatter.format(currentDate))
    }


    fun isDate1GreaterThanDate2(date1: String, date2: String, inputFormat: String): Boolean {
        val simpleDateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        val dateOne = simpleDateFormat.parse(date1)
        val dateTwo = simpleDateFormat.parse(date2)
        return dateTwo?.before(dateOne) == true && (dateTwo == dateOne).not()
    }

    fun getAge(year: Int, month: Int, day: Int): String {
        var age: Int

        val calenderToday = Calendar.getInstance()
        val currentYear = calenderToday.get(Calendar.YEAR)
        val currentMonth = 1 + calenderToday.get(Calendar.MONTH)
        val todayDay = calenderToday.get(Calendar.DAY_OF_MONTH)

        age = currentYear - year

        if (month > currentMonth) {
            --age
        } else if (month == currentMonth) {
            if (day > todayDay) {
                --age
            }
        }
        return age.toString()
    }

    fun timestampToDateString(requireFormat : String ="MM-dd-yyyy",dates: Long): String {
        val date = Date(dates)
        // format of the date
        val simpleDateFormat = SimpleDateFormat(requireFormat)
        simpleDateFormat.timeZone = TimeZone.getTimeZone(getTimeZone())
        return simpleDateFormat.format(date)
    }

    fun getTimeZone(): String {
        val tz = TimeZone.getDefault()
        Timber.e("Time Zone", "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezone id :: " + tz.id)
        return tz.id
    }

    fun getTimeZoneDisplayName(): String {
        val tz = TimeZone.getDefault()
        Timber.e("Time Zone", "TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezone id :: " + tz.id)
        return tz.getDisplayName(false, TimeZone.SHORT)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateStringToLocalToGMT(format: String, dateStr: String): String {
        if (dateStr == "" || dateStr == " ") {
            return ""
        }
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(SimpleDateFormat(format).parse(dateStr))
    }

    @SuppressLint("SimpleDateFormat")
    fun gmtToLocalDate(format: String, s: String): String {
        if (s == "" || s == " ") {
            return ""
        }
        val date = SimpleDateFormat(format).parse(s)
        val timeZone = getTimeZone()
        val local = Date(date.time + TimeZone.getTimeZone(timeZone).getOffset(date.time))
        return local.toString()
    }

    fun timeConversionBetweenTimeZone(fromFormatString: String, fromTimeZone: String, fromDateString: String, toFormatString: String, toTimeZone: String): String {
        val fromDateFormatter = SimpleDateFormat(fromFormatString)  //"yyyy-MM-dd HH:mm:ss"
        fromDateFormatter.timeZone = TimeZone.getTimeZone(fromTimeZone) //"Local TimeZone" or like "EST"
        try {
            val fromDate = fromDateFormatter.parse(fromDateString)
            val toDateFormatter = SimpleDateFormat(toFormatString)
            toDateFormatter.timeZone = TimeZone.getTimeZone(toTimeZone)
            return toDateFormatter.format(fromDate)
        } catch (e: Exception) {
            return ""
        }
    }

    fun getReadingAge(fromFormat: String, startDateSting: String, endDateString: String?): String {
        if (startDateSting.isEmpty()) {
            return "--"
        }
        val dateFormat = SimpleDateFormat(fromFormat)

        try {
            val oldDate = dateFormat.parse(startDateSting)
            val currentDate = Date()

            val diff = currentDate.time - oldDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val month = days / 30
            val year = month / 12

            if (year > 0 && month > 0) {
                return "$year years, $month months ago"
            } else if (year > 0) {
                if (year == 1.toLong()) {
                    return "$year year ago"
                }
                return "$year years ago"
            } else if (month > 0 && days > 0) {
                return "$month months, $days days ago"
            } else if (month > 0) {
                if (month == 1.toLong()) {
                    return "$month month ago"
                }
                return "$month months ago"
            } else if (days > 0 && hours % 24 > 0) {
                return "$days days ${hours % 24} hours ago"
            } else if (days > 0) {
                if (days == 1.toLong()) {
                    return "$days day ago"
                }
                return "$days days ago"
            } else if (hours % 24 > 0 && minutes % 60 > 0) {
                return "${hours % 24} hours ${minutes % 60} minutes ago"
            } else if (hours % 24 > 0) {
                if (hours % 24 == 1.toLong()) {
                    return "${hours % 24} hour ago"
                }
                return "${hours % 24} hours ago"
            } else if (minutes % 60 > 0 && seconds % 60 > 0) {
                return "${minutes % 60} minutes ${seconds % 60} seconds ago"
            } else if (minutes % 60 > 0) {
                if (minutes % 60 == 1.toLong()) {
                    return "${minutes % 60} minute ago"
                }
                return "${minutes % 60} minutes ago"
            } else if (seconds > 0) {
                if (seconds == 1.toLong()) {
                    return "$seconds second ago"
                }
                return "$seconds seconds ago"
            } else return ""

            /* if (oldDate.before(currentDate)) {

                 CLog.e("oldDate", "is previous date")
                 CLog.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes + " hours: " + hours + " days: " + days)

                 return "Difference:  seconds: $seconds minutes: $minutes hours: $hours days: $days"

             } else return ""*/


        } catch (e: ParseException) {
            e.printStackTrace()

            return ""
        }

    }

    enum class TimeDifference constructor(val timeDifference: Int) {
        SECOND(0), MINUTE(1), HOUR(2), DAY(3), MONTH(4), YEAR(5)
    }

    fun dateTimeDifference(currentTime: Long, tokenExpireTime: Long, timeDifference: TimeDifference): Long {
        if (timeDifference.timeDifference == 0) { //DIFFERENCE WITH SECONDS
            return (tokenExpireTime - currentTime) / 1000
        } else if (timeDifference.timeDifference == 1) { //DIFFERENCE WITH MINUTES
            return ((tokenExpireTime - currentTime) / 1000) / 60
        } else if (timeDifference.timeDifference == 2) { //DIFFERENCE WITH HOURS
            return (((tokenExpireTime - currentTime) / 1000) / 60) / 60
        } else if (timeDifference.timeDifference == 3) { //DIFFERENCE WITH DAYS
            return ((((tokenExpireTime - currentTime) / 1000) / 60) / 60) / 24
        } else if (timeDifference.timeDifference == 4) { //DIFFERENCE WITH MONTHS
            return (((((tokenExpireTime - currentTime) / 1000) / 60) / 60) / 24) / 30
        } else { //GETTING DIFFERENCE WITH YEARS
            return ((((((tokenExpireTime - currentTime) / 1000) / 60) / 60) / 24) / 30) / 365
        }
    }

    fun stringToMillis(format : String = "MM-dd-yyyy",dateString: String): Long {
        val dateFormat = SimpleDateFormat(format)
        val longMillis = dateFormat.parse(dateString)
        return longMillis?.time ?: 0
    }

}