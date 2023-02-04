package com.udacity.asteroidradar.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Formatter {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateToDay(date:LocalDateTime):String{
        val formatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
        return date.format(formatter)
    }
}