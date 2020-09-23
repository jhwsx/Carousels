package com.github.carousels.util

import android.util.Log

/**
 * Carousels 日志类
 *
 * @author wangzhichao
 * @date 2020-9-23
 */
class CarouselsLog {
    companion object {
        @JvmField
        var carouselsDebug = false
        @JvmStatic
        fun d(tag: String, message: String) {
            if (carouselsDebug) {
                Log.d(tag, message)
            }
        }
    }
}