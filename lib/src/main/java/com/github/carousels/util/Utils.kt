package com.github.carousels.util

import android.util.Log
import com.github.carousels.BuildConfig

var debug = false
fun logd(tag: String, message: String) {
    if (debug) {
       Log.d(tag, message)
    }
}