package com.github.carousels.util

import android.util.Log
import com.github.carousels.BuildConfig


fun logd(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
       Log.d(tag, message)
    }
}