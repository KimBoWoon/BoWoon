package com.bowoon.commonutils

import android.util.Log

object Log {
    private const val showLog = true

    fun i(tag: String, msg: String) {
        if (showLog) Log.i(tag, getMessageWithLineNumber(msg))
    }

    fun i(msg: String) {
        if (showLog) Log.i(tag(), getMessageWithLineNumber(msg))
    }

//    fun i(msg: String, tr: Throwable? = null) {
//        if (showLog) Log.i(tag(), getMessageWithLineNumber(msg), tr)
//    }

    fun v(tag: String, msg: String) {
        if (showLog) Log.v(tag, getMessageWithLineNumber(msg))
    }

    fun v(msg: String) {
        if (showLog) Log.v(tag(), getMessageWithLineNumber(msg))
    }

//    fun v(msg: String, tr: Throwable? = null) {
//        if (showLog) Log.v(tag(), getMessageWithLineNumber(msg), tr)
//    }

    fun d(tag: String, msg: String) {
        if (showLog) Log.d(tag, getMessageWithLineNumber(msg))
    }

    fun d(msg: String) {
        if (showLog) Log.d(tag(), getMessageWithLineNumber(msg))
    }

//    fun d(msg: String, tr: Throwable? = null) {
//        if (showLog) Log.d(tag(), getMessageWithLineNumber(msg), tr)
//    }

    fun w(tag: String, msg: String) {
        if (showLog) Log.d(tag, getMessageWithLineNumber(msg))
    }

    fun w(msg: String) {
        if (showLog) Log.w(tag(), getMessageWithLineNumber(msg))
    }

//    fun w(msg: String, tr: Throwable? = null) {
//        if (showLog) Log.w(tag(), getMessageWithLineNumber(msg), tr)
//    }

    fun e(tag: String, msg: String) {
        if (showLog) Log.d(tag, getMessageWithLineNumber(msg))
    }

    fun e(msg: String) {
        if (showLog) Log.e(tag(), getMessageWithLineNumber(msg))
    }

//    fun e(msg: String, tr: Throwable? = null) {
//        if (showLog) Log.e(tag(), getMessageWithLineNumber(msg), tr)
//    }

    fun printStackTrace(tr: Throwable? = null) {
        if (showLog) tr?.printStackTrace()
    }

    private fun tag(): String =
        Thread.currentThread().stackTrace.let { trace ->
            var index = 4

            while (index < trace.size && trace[index].fileName.isNullOrEmpty()) {
                index++
            }

            return when {
                trace.size > index -> "(${trace[index].fileName}:${trace[index].lineNumber})"
                else -> "LinkNotFound"
            }
        }

    private fun getMessageWithLineNumber(msg: String): String {
        Thread.currentThread().stackTrace.let { trace ->
            var index = 4

            while (index < trace.size && trace[index].fileName.isNullOrEmpty()) {
                index++
            }

            return when {
                trace.size > index -> "(${trace[index].fileName}:${trace[index].lineNumber}) $msg"
                else -> msg
            }
        }
    }
}