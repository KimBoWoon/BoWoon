package com.data.base.util

import android.util.Log

object Log {
    private const val showLog = true

    fun i(msg: String) {
        if (showLog) Log.i(tag(), msg)
    }

    fun i(msg: String, tr: Throwable?) {
        if (showLog) Log.i(tag(), msg, tr)
    }

    fun v(msg: String) {
        if (showLog) Log.v(tag(), msg)
    }

    fun v(msg: String, tr: Throwable?) {
        if (showLog) Log.v(tag(), msg, tr)
    }

    fun d(msg: String) {
        if (showLog) Log.d(tag(), msg)
    }

    fun d(msg: String, tr: Throwable?) {
        if (showLog) Log.d(tag(), msg, tr)
    }

    fun w(msg: String) {
        if (showLog) Log.w(tag(), msg)
    }

    fun w(msg: String, tr: Throwable?) {
        if (showLog) Log.w(tag(), msg, tr)
    }

    fun e(msg: String) {
        if (showLog) Log.e(tag(), msg)
    }

    fun e(msg: String, tr: Throwable?) {
        if (showLog) Log.e(tag(), msg, tr)
    }

    fun printStackTrace(tr: Throwable?) {
        if (showLog) tr?.printStackTrace()
    }

    private fun tag(): String? {
        Thread.currentThread().stackTrace[4]?.apply {
            val className = className.substring(className.lastIndexOf(".") + 1)
            val linkString = "(${fileName}:${lineNumber})"
            val pathString = "BoWoon# $className.${methodName}"
            return pathString + linkString
//            return if (pathString.length + linkString.length > 80) {
//                pathString.substring(0, 80 - linkString.length) + "..." + linkString
//            } else {
//                pathString + linkString
//            }
        }
        return null
    }
}