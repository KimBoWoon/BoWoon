package com.bowoon.commonutils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlin.math.roundToInt

object ScreenUtils {
    val Int.dp: Int get() = (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Int.dp(): Int = (this.toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()

object ContextUtils {
    fun Context?.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    fun Context?.showToast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
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

    fun Context?.sixteenByNineHeight(): Int {
        this ?: run {
            Log.e("getScreenWidth context is null!")
            return 0
        }

        val sixteenByNineRate = 9f / 16f

        return ((getScreenWidth()?.toFloat() ?: 0f) * sixteenByNineRate).toInt()
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

    fun hideIME(et: EditText?) {
        et ?: return
        (et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            et.windowToken,
            0
        )
    }

    fun showIME(et: EditText?, flag: Int = 0) {
        et ?: return
        if (et.requestFocus()) {
            (et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                et,
                flag
            )
        }
    }
}

fun <A, B, R> ifNotNull(obj1: A?, obj2: B?, action: (A, B) -> R) {
    if (obj1 != null && obj2 != null) {
        action.invoke(obj1, obj2)
    }
}

private fun resizeBitmap(context: Context, bitmap: Bitmap): Bitmap? {
    val rate: Float
    val bitmapWidth = bitmap.width.toFloat()
    val bitmapHeight = bitmap.height.toFloat()
    var newWidth = bitmapWidth
    var newHeight = bitmapHeight
    val clientHeight = getClientHeight(context).toFloat()

    if (bitmapWidth == 0f || bitmapHeight == 0f || newWidth == 0f || newHeight == 0f || clientHeight == 0f) {
        return null
    }

    if (bitmapWidth >= bitmapHeight) {
        if (bitmapWidth > clientHeight) {
            rate = clientHeight / bitmapWidth
            newHeight = bitmapHeight * rate
            newWidth = clientHeight
        }
    } else {
        if (bitmapHeight > clientHeight) {
            rate = clientHeight / bitmapHeight
            newWidth = bitmapWidth * rate
            newHeight = clientHeight
        }
    }

    return Bitmap.createScaledBitmap(bitmap, newWidth.toInt(), newHeight.toInt(), true)
}

fun getClientHeight(context: Context?): Int {
    context ?: return -1
    try {
        return if (context is Activity) {
            val rect = Rect()
            context.window.decorView.getWindowVisibleDisplayFrame(rect)
            // 인디케이터 영역 높이
            getScreenHeight(context) - rect.top
        } else {
            getScreenHeight(context)
        }
    } catch (e: java.lang.Exception) {
        Log.printStackTrace(e)
    }
    return -1
}

fun getScreenHeight(context: Context): Int {
    val height: Int = try {
        context.resources.displayMetrics.heightPixels
    } catch (e: java.lang.Exception) {
        val displayMetrics = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        wm?.defaultDisplay?.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
    return height
}