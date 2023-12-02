package com.data.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

object ScreenUtils {
    val Int.dp: Int get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()
}

object ContextUtils {
    fun Context?.showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration).show()
    }

    fun Context?.showSnackBar(
        view: View,
        message: String,
        duration: Int,
        actionText: String? = null,
        action: View.OnClickListener? = null
    ) {
        this?.let { context ->
            Snackbar.make(context, view, message, duration).run {
                ifNotNull(actionText, action) { actionMessage, action ->
                    setAction(actionMessage, action)
                }
                show()
            }
        }
    }

    fun Context?.showSnackBar(
        view: View,
        message: String,
        duration: Int,
        @StringRes actionResId: Int? = null,
        action: View.OnClickListener? = null
    ) {
        this?.let { context ->
            Snackbar.make(context, view, message, duration).run {
                ifNotNull(actionResId, action) { actionMessage, action ->
                    setAction(actionMessage, action)
                }
                show()
            }
        }
    }

    fun Context?.getScreenWidth(): Int? {
        this ?: run {
            Log.e("getScreenWidth context is null!")
            return null
        }

        (this.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.apply {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                currentWindowMetrics.run {
                    windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).let {
                        bounds.width() - it.left - it.right
                    }
                }
            } else {
                DisplayMetrics().run {
                    defaultDisplay.getMetrics(this)
                    widthPixels
                }
            }
        }

        return null
    }

    fun Context?.getScreenHeight(): Int? {
        this ?: run {
            Log.e("getScreenHeight context is null!")
            return null
        }

        (this.getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.apply {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                currentWindowMetrics.run {
                    windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).let {
                        bounds.height() - it.bottom - it.top
                    }
                }
            } else {
                DisplayMetrics().run {
                    defaultDisplay.getMetrics(this)
                    heightPixels
                }
            }
        }

        return null
    }
}

object ViewUtils {
    fun View?.showSnackBar(
        message: String,
        duration: Int,
        actionText: String? = null,
        action: View.OnClickListener? = null
    ) {
        this?.let { view ->
            Snackbar.make(view, message, duration).run {
                ifNotNull(actionText, action) { actionMessage, action ->
                    setAction(actionMessage, action)
                }
                show()
            }
        }
    }

    fun View?.showSnackBar(
        message: String,
        duration: Int,
        @StringRes actionResId: Int? = null,
        action: View.OnClickListener? = null
    ) {
        this?.let { view ->
            Snackbar.make(view, message, duration).run {
                ifNotNull(actionResId, action) { actionMessage, action ->
                    setAction(actionMessage, action)
                }
                show()
            }
        }
    }

    fun View?.showSnackBar(
        @StringRes resId: Int,
        duration: Int,
        actionText: String? = null,
        action: View.OnClickListener? = null
    ) {
        this?.let { view ->
            Snackbar.make(view, resId, duration).run {
                ifNotNull(actionText, action) { actionMessage, action ->
                    setAction(actionMessage, action)
                }
                show()
            }
        }
    }

    fun View?.showSnackBar(
        @StringRes resId: Int,
        duration: Int,
        @StringRes actionResId: Int? = null,
        action: View.OnClickListener? = null
    ) {
        this?.let { view ->
            Snackbar.make(view, resId, duration).run {
                ifNotNull(actionResId, action) { actionMessage, action ->
                    setAction(actionMessage, action)
                }
                show()
            }
        }
    }

    fun View?.hideSoftKeyboard() {
        this ?: run {
            Log.e("hideSoftKeyboard view is null")
            return
        }

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    fun View?.showSoftKeyboard() {
        this ?: run {
            Log.e("hideSoftKeyboard view is null")
            return
        }

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(this, 0)
    }
}

fun <A, B, R> ifNotNull(obj1: A?, obj2: B?, action: (A, B) -> R) {
    if (obj1 != null && obj2 != null) {
        action.invoke(obj1, obj2)
    }
}