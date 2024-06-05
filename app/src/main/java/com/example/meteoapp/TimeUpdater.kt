package com.example.meteoapp

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class TimeUpdater(private val tvCurrentTime: TextView) {

    private val handler = Handler(Looper.getMainLooper())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    fun startUpdatingTime() {
        updateTime()
    }

    private fun updateTime() {
        val currentTime = timeFormat.format(Date())
        tvCurrentTime.text = currentTime

        handler.postDelayed({
            updateTime()
        }, 1000)
    }
}
